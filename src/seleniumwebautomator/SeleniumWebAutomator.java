
package seleniumwebautomator;

import seleniumwebautomator.utils.CSVFileUtilities;

/**
 *
 * @author amoi
 */
public class SeleniumWebAutomator {

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws Exception {

     System.out.println("System size:"+CSVFileUtilities.readData(",", 
             "/Users/amoi/Projects_Documentation/2kuze/2kuze_nandi/"
                     + "Testing/Test_Automation/server/csfFile.csv"));
    }

    public class Generic<T> {

        T type;

        public Class getGenericType() {
            return type.getClass();
        }

        @Override
        public String toString() {
            return "Type Parameter  is "
                    + getGenericType().getName() + ".";
        }
    }

}
