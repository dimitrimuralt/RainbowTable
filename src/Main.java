import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        char alphabet[] = {
                '0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z'};



        ArrayList<String> dictionary = new ArrayList<>();

        // creating 2000 passwords 0000000 [...] 00001jj
        for(int i = 0; i<2000; i++){
            //convert int to string with base 36, 36 is the length of the alphabet
            String s = Integer.toString(i, 36);
            while(s.length() < 7) s = "0".concat(s); //fill up leading 0
            dictionary.add(s);
        }

        RainbowTableGenerator rainbowTableGenerator = new RainbowTableGenerator(alphabet, dictionary, 2000);

        MD5Cracker md5Cracker = new MD5Cracker(rainbowTableGenerator.getRainbowTable(), alphabet);
        String hashToCrack = "1d56a37fb6b08aa709fe90e12ca59e12";
        String crackedPassword = md5Cracker.crack(hashToCrack); //should be 0bgec3d

        if(crackedPassword != null){System.out.println("Matching password to "+hashToCrack+" is "+crackedPassword);}
        else System.out.println("No password found");
    }


}
