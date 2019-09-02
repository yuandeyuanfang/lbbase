package com.lb.base.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class StringUtil {
	public static boolean isEmpty(String string){
		return (string == null || "".equalsIgnoreCase(string.trim()));
	}
	
	public static String cutFileSuffix(String fileName){
		if(fileName==null || "".equals(fileName.trim()))
			return "";
		int index = fileName.lastIndexOf('.');
		if(index<0)
			return "";
		else{
			return fileName.substring(index+1);
		}
	}
	
	public static String buildAutoFileName(String fileName){
		String suffix = cutFileSuffix(fileName);
		Date date = new Date();
		String newFileName = date.getTime()+"."+suffix;
		return newFileName;
	}
	
	public static String getTodayString(){
		return DateToStr(new Date());
	}
	
	public static String DateToStr(Date date, String format){
		return new java.text.SimpleDateFormat(format).format(date);
	}
	
	public static String DateToStr(Date date){
		return DateToStr(date,"yyyy-MM-dd");
	}
	
	public static String ParameterUnit(String url){
		if(isEmpty(url))
			return "?";
		else
			return url+"&";
	}
	public static String DateToStrOracle(Date date, String format){
		String oracleFormat = format;
		if("yyyy-MM-dd HH:mm:ss".equals(format))
			oracleFormat= "yyyy-mm-dd HH24:MI:SS";
		else if("yyyy-MM-dd HH:mm".equals(format))
			oracleFormat= "yyyy-mm-dd HH24:MI";
		else if("yyyy-MM-dd HH".equals(format))
			oracleFormat= "yyyy-mm-dd HH24";
		return "to_date('"+DateToStr(date,format)+"','"+oracleFormat+"')";
	}
	
	/**
	 * 当天结算开始时间,即当天的00:00
	 * @param date
	 * @return
	 */
	public static String BalanceDateToStrOracle(Date date){
		String balanceStr = DateToStr(date) + " 00:00";
		return "to_date('"+balanceStr+"','yyyy-mm-dd HH24:MI')";
	}
	/**
	 * 结算结束时间,明天的凌晨 00:00
	 * @param date
	 * @return
	 */
	public static String NextBalanceDateToStrOracle(Date date){
		Date nextDate = DateUtils.getNextDayCurrDay(date, 1);
		String balanceStr = DateToStr(nextDate) + " 00:00";
		return "to_date('"+balanceStr+"','yyyy-mm-dd HH24:MI')";
	}
	/**
	 * 结算时间，一般取上一天的 00:00
	 * @param date
	 * @return
	 */
	public static String LastBalanceDateToStrOracle(Date date){
		Date lastDate = DateUtils.getNextDayCurrDay(date, -1);
		String balanceStr = DateToStr(lastDate) + " 00:00";
		return "to_date('"+balanceStr+"','yyyy-mm-dd HH24:MI')";
	}
	/**
	 * 结算时间，一般取当天的 16:00
	 */
	public static String DateToStrOracle(Date date){
		return DateToStrOracle(date,"yyyy-MM-dd");
	}
	public static String DateToStrOracleSecond(Date date){
		return DateToStrOracle(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static String StrDateToStrOracle(String date,String format) throws ParseException{
			Date d = new java.text.SimpleDateFormat(format).parse(date);
			return DateToStrOracle(d,"yyyy-MM-dd");		
	}
	public static String MoneyToStr(BigDecimal bigDecimal){
		return new java.text.DecimalFormat("###.00").format(bigDecimal);
	}
	
	public static String NextDateToStrOracle(Date date,String format,int amount){
		Date nextDate = DateUtils.getNextDayCurrDay(date, amount);
		String oracleFormat = format;
		if("yyyy-MM-dd HH:mm:ss".equals(format))
			oracleFormat= "yyyy-mm-dd HH24:MI:SS";
		else if("yyyy-MM-dd HH:mm".equals(format))
			oracleFormat= "yyyy-mm-dd HH24:MI";
		else if("yyyy-MM-dd HH".equals(format))
			oracleFormat= "yyyy-mm-dd HH24";
		return "to_date('"+DateToStr(nextDate,format)+"','"+oracleFormat+"')";
	}
	
	public static String NextDateToStrOracle(Date date,String format){
		return NextDateToStrOracle(date,format,1);
	}
	
	public static String NextDateToStrOracle(Date date){
		return NextDateToStrOracle(date,"yyyy-MM-dd");
	}
	
	public static String NextDateToStrOracle(Date date,int amount){
		return NextDateToStrOracle(date,"yyyy-MM-dd",amount);
	}
	public static String checkLinker(String condition) {
		return isEmpty(condition)?"":"&";
	}
	public static String checkSQLLinker(String condition) {
		return isEmpty(condition)?"":"and";
	}
	public static String convertSQLBlank(String sql,boolean bIn){
        if(bIn)
            return sql.replace(' ','?');
        else
            return sql.replace('?',' ');
    }
    public static String convertNULLTrim(String txt){
    	if(txt==null)	return "";
    	return txt.trim();
    }
    /**
     * 去除空格和逗号分号转换
     * @param cont
     * @return
     */
    public static String convertBalnkAndSpliter(String cont){       
        return cont.replaceAll(" ","").replaceAll(",","，").replaceAll(";","；");     
    }
        
       
    
    /*
	 * 字符长度处理,过长切除,不足补空格
	 * 传入长度为0什么都不操作
	 */
	public static String StringBatch(String input,int length){
		String ret = input.trim();
		//int len = input.getBytes().length;
		if(null!=ret){
			
			if(0==length){//0直接返回原来字符串
				return ret;
			}
			
			int len = ret.getBytes().length;
			if(len<=length){
				
				for(int j = 0;j<(length-len);j++){
					ret = ret + " ";
				}
			}else{
				
				if(len==ret.length()){
					ret = ret.substring(0,length);
				}else{
					ret = ret.substring(0,length/2);//.toString();//ret.split("",length);
				}
			}
		}else{//空字符串
			ret = "";
			for(int j = 0;j<(length);j++){
				ret = ret + " ";
			}
		}
		return ret;
	}
	/**********组装字符串***********/
	public static String unitString(String[] a,String b)
	{
		String result="";
		if(a!=null && a.length>0)
		{
			for(int i=0;i<a.length;i++)
			{
				result+=a[i]+b;
			}
		}
		return result;
	}
	
	/**
	 * 过滤空字符串或者以及去掉二头多有空格
	 * @param string
	 * @return
	 */
	public static String filterNullString(String string){
		if(isEmpty(string))
			return "";
		return string.trim();
	}
	public static String toString(Object[] array) {
		int len = array.length;
		if ( len == 0 ) return "";
		StringBuffer buf = new StringBuffer( len * 12 );
		for ( int i = 0; i < len - 1; i++ ) {
			buf.append( array[i] ).append(", ");
		}
		return buf.append( array[len - 1] ).toString();
	}
	/**
	 * 获取银行卡号的掩码
	 * @param cardNo
	 * 		真实银行卡号
	 * @return
	 */
	public static String getBankShade(String cardNo){
		if(StringUtil.isEmpty(cardNo))
			return "";
		if(cardNo.length()<8)
			return cardNo;
		if(cardNo.length()<8)
			return cardNo;
		String pre = cardNo.substring(0,4);
		String pix = cardNo.substring(cardNo.length()-4);
		String middle = cardNo.substring(4,cardNo.length()-4);
		String shard = "";
		for(int i=0; i<middle.length(); i++){
			shard += "*";
		}
		return pre + shard + pix;
	}
	/**
	 * 连接两个字符串，去掉连接部分重复的地方。
	 * 例如把字符串：上海虹桥、虹桥国际机场链接成一个字符串，中间的虹桥不能重复，即上海虹桥国际机场
	 * @author XuGuo
	 * @since 2009-10-30
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String trimjoinString(String str1,String str2){
		if(str1==null&&str2==null){
			return "";
		}else if(str1!=null&&str2==null){
			return str1;
		}else if(str1==null&&str2!=null){
			return str2;
		}else{
			int repeatTemp = 0,maxRepeat = 0;
			int str1Len = str1.length();
			int str2Len = str2.length();
			int minLen = 0;
			if(str1Len>str2Len){
				minLen = str2Len;
			}else{
				minLen = str1Len;
			}
			for(int i=0;i<minLen;i++){
				repeatTemp = 0;
				for(int k=str1Len-i-1,j=0;k<str1Len&&j<str2Len&&j<=i;k++,j++){
					if(str1.charAt(k)==str2.charAt(j)){
						repeatTemp++;
					}else{
						break;
					}
				}
				if(maxRepeat<repeatTemp){
					maxRepeat = repeatTemp;
				}
			}
			return str1 + str2.substring(maxRepeat);
		}
	}	

	  public static final String EMPTY = "";
	  /*
      * a single String containing the provided elements.</p>
      *
      * <p>No delimiter is added before or after the list.
      * A <code>null</code> separator is the same as an empty String ("").</p>
      *
      * <p>See the examples here: {@link #join(Object[],String)}. </p>
      *
      * @param iterator  the <code>Iterator</code> of values to join together, may be null
      * @param separator  the separator character to use, null treated as ""
      * @return the joined String, <code>null</code> if null iterator input
      */
     public static String join(Iterator iterator, String separator) {
 
         // handle null, zero and one elements before building a buffer
         if (iterator == null) {
             return null;
         }
         if (!iterator.hasNext()) {
             return EMPTY;
         }
         Object first = iterator.next();
         if (!iterator.hasNext()) {
             return ObjectUtils.toString(first);
         }
 
         // two or more elements
         StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
         if (first != null) {
             buf.append(first);
         }
 
         while (iterator.hasNext()) {
             if (separator != null) {
                 buf.append(separator);
             }
             Object obj = iterator.next();
             if (obj != null) {
                 buf.append(obj);
             }
         }
         return buf.toString();
     }
     /**
      * <p>Joins the elements of the provided <code>Collection</code> into
      * a single String containing the provided elements.</p>
      *
      * <p>No delimiter is added before or after the list.
      * A <code>null</code> separator is the same as an empty String ("").</p>
      *
      * <p>See the examples here: {@link #join(Object[],String)}. </p>
      *
      * @param collection  the <code>Collection</code> of values to join together, may be null
      * @param separator  the separator character to use, null treated as ""
      * @return the joined String, <code>null</code> if null iterator input
      * @since 2.3
      */
	public static String join(Collection collection, String separator) {
         if (collection == null) {
             return null;
         }
         return join(collection.iterator(), separator);
     }
	
}
