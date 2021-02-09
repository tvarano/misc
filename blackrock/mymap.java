public class MyMap implements Map<String, String> {

    private String[][] data;

    public MyMap() {
        // insert the data
    }


    public String get(String key) {

    }
    // get
    // put

}




// TSLA -> Price() : highestPrice, lowestPrice, averagePrice
//highestValue getHighestValue(Map<String, Price> holdings)
//sum of all of the highestPrices in your holdings TSLA, AAPL....
// highest value of your holdings at the time

public double peak(Map<String, Price> holdings) {

    double sum = 0;
    // for k in holdings, sum all map.get(k).highestPrice
    for (String s : holdings.keySet()) {
        sum += holdings.get(s).highestPrice;
    }   
    
    return sum;
}



//List of numbers upto n 1 to 50; 47 
public int missing(int[] list) {
    int sum;
    for (int i : list) {
        sum += i;
    }
    return 1225 - sum;
}



//Find out the common longest sub string if you are given two strings
public String longestSub(String a, String b) {
    String curLong = "";
    String mLong;
  
    for (char c: a.toCharArray[]) {
        curLong += c;
        if (!b.contains(curLong)) {
            if (curLong.length() > mLong.length())
                mLong = curLong;
            curLong = c + "";
        } 
    }
    if (curLong.length() > mLong.length())
        mLong = curLong;
        
    return mLong;
    
}




// String: "ahdddkljanmalkjdapweoij"
public void removeDups(String s) {
    boolean[] found = new boolean[26];
    
    for (char c: s.toCharArray()) {
        int ivalue = Math.abs('a' - c);
        found[value] = true;
    }
    
    for (int i = 0; i < found.length; i++) {
        if (found[i])
            System.out.print((char)('a' + i));
    }
    System.out.println();
}



