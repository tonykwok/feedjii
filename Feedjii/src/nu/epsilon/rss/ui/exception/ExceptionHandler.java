package nu.epsilon.rss.ui.exception;

import nu.epsilon.rss.ui.slotmachine.SlotMachineExceptionHandler;

/**
 *
 * @author Pär Sikö
 */
public class ExceptionHandler {
    
    private SlotMachineExceptionHandler slotMachine;

    public ExceptionHandler() {
        slotMachine = SlotMachineExceptionHandler.getInstance();
    }

    public void showExceptionOnGlassPane(Exception exc) {

        if (!slotMachine.isRunning()) {
            slotMachine.setException(exc);
            System.out.println("Starting slot machine");
            slotMachine.startSlotMachine();
        }
    }
}
