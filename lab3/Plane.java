import java.util.Arrays;
import java.util.Comparator;

public class Plane {
    private PlaneSeat[] seat;
    private int numEmptySeat;

    public Plane() {
        seat = new PlaneSeat[12];
        for (int i = 0; i < seat.length; i++) {
            seat[i] = new PlaneSeat(i + 1);
        }
        numEmptySeat = seat.length;
    }

    private PlaneSeat[] sortSeats() {
        PlaneSeat[] copy = Arrays.copyOf(seat, seat.length);
        Arrays.sort(copy, Comparator.comparingInt(PlaneSeat::getCustomerID));
        return copy;
    }

    public void showNumEmptySeats() {
        System.out.println("Number of empty seats: " + numEmptySeat);
    }

    public void showEmptySeats() {
        System.out.print("Empty seats: ");
        boolean first = true;
        for (PlaneSeat s : seat) {
            if (!s.isAssigned()) {
                if (!first) System.out.print(", ");
                System.out.print(s.getSeatID());
                first = false;
            }
        }
        System.out.println();
    }

    public void showAssignedSeats(boolean bySeatId) {
        PlaneSeat[] toShow = bySeatId ? seat : sortSeats();
        System.out.println("Assigned seats:");
        for (PlaneSeat s : toShow) {
            if (s.isAssigned()) {
                System.out.println("SeatID " + s.getSeatID() +
                                   " -> CustomerID " + s.getCustomerID());
            }
        }
    }

    public void assignSeat(int seatId, int cust_id) {
        PlaneSeat s = findBySeatId(seatId);
        if (s == null) { System.out.println("Invalid seat ID."); return; }
        if (s.isAssigned()) { System.out.println("Seat " + seatId + " already assigned."); return; }
        s.assign(cust_id);
        numEmptySeat--;
        System.out.println("Seat " + seatId + " assigned to customer " + cust_id + ".");
    }

    public void unAssignSeat(int seatId) {
        PlaneSeat s = findBySeatId(seatId);
        if (s == null) { System.out.println("Invalid seat ID."); return; }
        if (!s.isAssigned()) { System.out.println("Seat " + seatId + " is already empty."); return; }
        s.unassign();
        numEmptySeat++;
        System.out.println("Seat " + seatId + " unassigned.");
    }

    private PlaneSeat findBySeatId(int seatId) {
        if (seatId < 1 || seatId > seat.length) return null;
        return seat[seatId - 1];
    }
}
