import java.util.HashMap;

public class MD5Cracker {
    private HashMap<String, String> rainbowTable;
    private int chainLength;
    private char[] alphabet;
    private int passwordLength;

    public MD5Cracker(HashMap<String, String> rainbowTable, char[] alphabet){
        this.rainbowTable = rainbowTable;
        this.chainLength = rainbowTable.size();
        this.alphabet = alphabet;
        this.passwordLength = rainbowTable.keySet().stream().findAny().get().length();
    }

    /***
     * first the method findChainEndValueForHash is called. It returns the first matching chain end value
     * which potentially could contain the hash to be found
     *
     * for matched chain end value the start value is retrieved and the chain generation method is called,
     * which returns a matching password if found.
     *
     * @param hashToCrack       hash where a corresponding password is needed
     *
     * @return a matching password to the received hash or null if not found
     */
    public String crack(String hashToCrack){
        String crackedPassword = null;

        String matchedEndValue = findChainEndValueForHash(hashToCrack);
        if(matchedEndValue == null) return null;

            String passwordStart = getChainStartValue(matchedEndValue);
            System.out.println("Chain end value matched: " + matchedEndValue + " corresponding start value is " + passwordStart);

            String password = searchPasswordInChain(passwordStart, hashToCrack);
            if(password != null){ crackedPassword = password;
                System.out.println(hashToCrack + " found in chain starting with " +
                                   passwordStart + " matching password: " +
                                   password);
            }
            else {
                System.out.println(hashToCrack +" not found in chain starting with "+passwordStart);
            }
        return crackedPassword;
    }

    /***
     * Searches a hash in a chain and returns the corresponding password
     * The chain to be generated from the start password chain length times
     * because the whole chain isn't stored in the rainbowTable
     *
     * @param passwordStart    password the chain begins with
     * @param hashToFind       hash to be found in chain
     *
     * @return corresponding password to the recieved hash, renurns empty String if not found
     */
    public String searchPasswordInChain(String passwordStart, String hashToFind){
        String password = null;
        String currentHash;
        String passwordCurrent = passwordStart;

        for(int i= 0; i<chainLength; i++){
            currentHash = Util.hash(passwordCurrent);
            if(currentHash.equals(hashToFind)) {
                password = passwordCurrent;
            }
            passwordCurrent = Util.reduce(currentHash,i, alphabet, passwordLength);
        }
        return password;
    }

    /***
     * return the corresponding chain start value to a given chain end value
     *
     * @param chainEndValue    chain end value
     *
     * @return corresponding chain start value to the chain en value
     */
    public String getChainStartValue(String chainEndValue){

        for(Object o : rainbowTable.keySet()){
            if(chainEndValue.equals(rainbowTable.get(o))) {
                return String.valueOf(o);
            }
        }
        return null;
    }


    /***
     * searches the rainbowTable for the the hash to be cracked. The hash itself isn't stored
     * in the rainbowTable, this method just returns a chain end value
     * which potentially could contain the hash to be found
     *
     * beginning with the received hash and last round a chain is generated and compared each time to the
     * end values in the rainbowTable, if an end value matches it's returned
     *
     * the search stops, when a chain end value has matched, that means, if there is a collision in the same round
     * the hash can't be found
     *
     * @param hashToCrack    hash to be found
     *
     * @return a String chain end values which potentially could contain the hash to be found
     */
    private String findChainEndValueForHash(String hashToCrack){
        String passwordCurrent;
        System.out.println("Searching password for: " + hashToCrack);

        //determine round for chain generation
        for(int a = chainLength; a>=0; a--){
            String hashNext = hashToCrack;
            //generate one rainbow chain starting with round a
            //a is max chain length at the beginning and decreases in each round
            for(int i = a; i<chainLength; i++){
                passwordCurrent = Util.reduce(hashNext,i, alphabet, passwordLength);
                //check if current password matches with an end value in rainbowtable
                if(rainbowTable.containsValue(passwordCurrent)) {
                    return passwordCurrent;
                }
                hashNext = Util.hash(passwordCurrent);
            }
        }
        return null;
    }

}
