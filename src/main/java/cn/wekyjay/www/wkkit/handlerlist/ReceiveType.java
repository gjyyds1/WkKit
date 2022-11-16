package cn.wekyjay.www.wkkit.handlerlist;

public enum ReceiveType{
	MAIL("MAIL"),
	SEND("SEND"),
	MENU("MENU"),
	GIVE("GIVE");
	
	private final String TYPE;
	
	private ReceiveType(String type) {
		this.TYPE = type;
	}
	
	@Override
	public String toString() {
		return TYPE;
	}
	
}
