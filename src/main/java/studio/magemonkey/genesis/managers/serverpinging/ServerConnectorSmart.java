package studio.magemonkey.genesis.managers.serverpinging;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.List;

public class ServerConnectorSmart implements ServerConnector {


    private final List<ServerConnector> modules;
    private       ServerConnector       current;
    private       int                   currentId = -1;
    private       boolean               hadSuccess;
    private       int                   failsAmount;

    public ServerConnectorSmart(List<ServerConnector> modules) {
        this.modules = modules;

        hadSuccess = ClassManager.manager.getStorageManager().getConfig().contains("serverpinging.connector");
        int connectorType = ClassManager.manager.getStorageManager().getConfig().getInt("serverpinging.connector");

        if (connectorType >= 0 && connectorType < modules.size()) {
            currentId = connectorType;
            current = modules.get(currentId);
        } else {
            next();
        }
    }


    public void next() {
        if (modules != null) {
            currentId++;
            if (modules.size() <= currentId) { //Reached end of modules
                currentId = 0;
            }
            current = modules.get(currentId);
        }
    }

    @Override
    public boolean update(ServerInfo info) {
        if (current != null) {

            if (info.isWaiting()) {
                return false;
            }

            info.setBeingPinged(true);
            if (current.update(info)) { // Success
                if (!hadSuccess) { // If has not had success yet -> save connector type
                    ClassManager.manager.getStorageManager().getConfig().set("serverpinging.connector", currentId);
                    Genesis.log("Saving ServerPinging Connector '" + current.getClass()
                            + "' to be instantly used next time.");
                    hadSuccess = true;
                    failsAmount = 0;
                }
                info.setBeingPinged(false);
                return true;

            } else { //No success
                info.hadNoSuccess();
                failsAmount++;

                boolean next = false;
                if (hadSuccess) { // If has had success already -> only try next connector when no success for ages
                    if (failsAmount > 50 & !ClassManager.manager.getSettings().getServerPingingFixConnector()) {
                        hadSuccess = false;
                        next = true;
                    }
                } else { // If has had no success yet and still none -> try next connector fast
                    if (failsAmount > 4) {
                        next = true;
                    }
                }

                if (next) {
                    ClassManager.manager.getBugFinder()
                            .warn("[ServerPinging] Connector '" + current.getClass()
                                    + "' does not seem to fit.. Trying an other Connector type.");
                    failsAmount = 0;
                    next();
                }
            }
        }
        info.setBeingPinged(false);
        return false;
    }


}
