import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Util {

    /***
     * hashes a given String with md5 algorithm
     * MessageDigest is a default java library, see documentation here:
     * //https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html
     *
     * @param s     String to be hashed
     * @return      hash as a String
     */
    public static String hash(String s){
        try {
            // set hashing algorithm to md5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // get hash for current string as byteArray
            byte[] hashByteString = md.digest(s.getBytes());
            //convert hash byteArray in a String
            return byteArrayToString(hashByteString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * converts hash byteArray to a String
     *
     * @param byteString    hash byteArray to be converted into a String
     * @return              hash as String
     */
    public static String byteArrayToString(byte[] byteString){
        String hashAsString = "";
        for(int i :  byteString){
            // & 0xff is needed to set the value of i to unsigned to that the
            // value range is between 0 and 255 instead of -128 und 127
            //if(i<0) i =+ 128;
            String toAdd = Integer.toHexString(0xff & i );
            //add leading zero if necessary
            if(toAdd.length()<2) toAdd = '0'+toAdd;
            hashAsString = hashAsString.concat(toAdd);
        }
        return hashAsString;
    }

    /***
     * reduces a hash using the algorithm from krysi course V3.27
     *
     * @param hash    hash to be reduced
     * @param round   current reduce round
     * @param alphabet characters the password is composed of
     * @param passwordLength lenght of password
     * @return               reduced password as String
     */
    public static String reduce(String hash, int round, char[] alphabet, int passwordLength){
        //convert the hash as string into a number with base 16
        BigInteger hashAsNumber = new BigInteger(hash,16);
        //add round number to hashAsNumber
        hashAsNumber = hashAsNumber.add(BigInteger.valueOf(round));

        BigInteger alphabetLength = BigInteger.valueOf(alphabet.length);

        String reducedString = "";
        BigInteger positionInAlphabet;

        //apply reduce function form krysi course V3.27
        for(int i = 0; i<passwordLength; i++){
            positionInAlphabet = hashAsNumber.mod(alphabetLength);
            hashAsNumber = hashAsNumber.divide(alphabetLength);
            reducedString = alphabet[positionInAlphabet.intValue()]+reducedString;
        }
        return reducedString;
    }
}
