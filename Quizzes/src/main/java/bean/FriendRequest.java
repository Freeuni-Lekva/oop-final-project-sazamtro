package bean;

public class FriendRequest {
    private int request_id;
    private int sender_id;
    private int receiver_id;
    private FriendRequestStatus status;
    private static final int DEFAULT_ID = -1;

    public FriendRequest(int request_id, int sender_id, int receiver_id, FriendRequestStatus status){
        this.request_id = request_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = status;
    }

    public FriendRequest(int sender_id, int receiver_id, FriendRequestStatus status){
        this.request_id = DEFAULT_ID;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = status;
    }

    public FriendRequest(int sender_id, int receiver_id){
        this.request_id = DEFAULT_ID;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = FriendRequestStatus.PENDING;
    }

    public int getRequestId(){return request_id;}

    public int getSenderId(){return sender_id;}

    public int getReceiverId(){return receiver_id;}

    public FriendRequestStatus getStatus(){return status;}

    public void setRequest_id(int request_id){this.request_id = request_id;}

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(! (o instanceof FriendRequest)) return false;
        FriendRequest other = (FriendRequest) o;
        return other.getRequestId() == this.getRequestId() &&
                other.getSenderId() == this.getSenderId() &&
                other.getReceiverId() == this.getReceiverId() &&
                other.getStatus() == this.getStatus();
    }

}
