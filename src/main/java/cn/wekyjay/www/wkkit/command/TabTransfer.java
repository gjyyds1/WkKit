package cn.wekyjay.www.wkkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public enum TabTransfer {
	
	
    FIRST(Arrays.asList("transfer"),0,null,new int[]{1}),
	KIT_NAME(Arrays.asList("MySQL"),1,"transfer",new int[]{2});
	
    private List<String> list;
    private int befPos;
    private String bef;
    private int[] num;
    
    
    
    private TabTransfer(List<String> list,int befPos, String bef, int[] num){
        this.list = list;
        this.befPos = befPos;
        this.bef = bef;
        this.num = num.clone();
    }
    
    public String getBef() {
		return bef;
	}
    public int getBefPos() {
		return befPos;
	}
    public List<String> getList() {
    	return list;
	}
    public int[] getNum() {
		return num;
	}
    
    
    public static List<String> returnList(String[] Para, int curNum, CommandSender sender) {
        for(TabTransfer tab : TabTransfer.values() ){
            if(tab.getBefPos()-1 >= Para.length){
                continue;
            }
            if((tab.getBef() == null || tab.getBef().equalsIgnoreCase(Para[tab.getBefPos()-1])) && Arrays.binarySearch(tab.getNum(),curNum)>=0){
            	List<String> list = new ArrayList<>();
            	if(!(Para[tab.getNum()[0] - 1] == null)) {
                	int length = Para[tab.getNum()[0] - 1].length();
                	String abc = Para[tab.getNum()[0] - 1];
                	for(String s : tab.getList()) {
                		if(s.regionMatches(true, 0, abc, 0, length)) list.add(s);
                	}
                	return list;
                }else {
                	return tab.getList();
                }
            }
        }
        return null;
    }
    
    
}
