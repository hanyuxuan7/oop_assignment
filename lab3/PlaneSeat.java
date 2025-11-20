public class PlaneSeat {
    private int seatId, customerId;
    private boolean assigned;
    public PlaneSeat(int seat_id) {
        this.seatId = seat_id;
        this.assigned = false;
        this.customerId = 0;
    }

    public int getSeatID() {
        return seatId;
    }

    public int getCustomerID() {
        return customerId;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void assign(int customer_id) {
        this.customerId = customer_id;
        this.assigned = true;
    }

    public void unassign() {
        this.customerId = 0;
        this.assigned = false;
    }
}