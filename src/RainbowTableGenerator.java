import java.util.*;


public class RainbowTableGenerator {
    private ArrayList<String> dictionary;
    private int chainLength;
    private char[] alphabet;
    private int passwordLength;

    private HashMap<String, String> rainbowTable = new HashMap<>();

    /***
     * Sets all the values needed for rainbow table generation and calls the generateRainbowTable() method
     */
    public RainbowTableGenerator(char[] alphabet, ArrayList<String> dictionary, int chainLength) {
        this.alphabet = alphabet;
        this.dictionary = dictionary;
        this.chainLength = chainLength;
        this.passwordLength = dictionary.get(0).length();

        generateRainbowTable();
    }

    /***
     * calls the create chain method for each password in dictionary
     */
    private void generateRainbowTable(){
        System.out.println("Generating rainbowtable for passwords "+
                dictionary.get(0) +
                " [...] " +
                dictionary.get(dictionary.size()-1)+
                " with chain length "+
                chainLength);
        for (String password: dictionary) {
            createChain(password);
        }
    }

    /***
     * creates a chain for a given password
     * a chain is created by alternate hashing and an reducing chainLength-times
     * chain length is set in the constructor
     *
     * after chain generation only the start and end value of the chain are stored in the rainbow table
     *
     * @param passwordStart  password the chain starts with
     */
    private void createChain(String passwordStart){
        String currentHash;
        String passwordCurrent = passwordStart;

        for(int i= 0; i<chainLength; i++){
            currentHash = Util.hash(passwordCurrent);
            passwordCurrent = Util.reduce(currentHash,i, alphabet, passwordLength);
        }
        rainbowTable.put(passwordStart,passwordCurrent);
    }


    public HashMap<String, String> getRainbowTable() {
        return rainbowTable;
    }
}
