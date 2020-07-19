/**
* The HashPassword class uses the MessageDigest class to 
* hash and salt passwords of users through SHA-256.
*
* @author  Filippos Kontogiannis
* @version 1.0
* @since   2020
*/

import java.security.MessageDigest; // Used for hashing
import java.security.NoSuchAlgorithmException; //Exception for when MessageDigest instance is initialized
import java.io.UnsupportedEncodingException; //Exception for when turning Strings into UTF-8 format

public class HashPassword
{
   /**
   * This method takes a password string and some salt string
   * and creates a hash through their concatenation.
   * @param p The actual string of the password
   * @param salt  A randomly generated number
   * @return String, returns the Hexadecimal form of the hash
   */
    public static String encrypt(String p, String salt)
    {
        try
        {
            
            try
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); //Initialize MessageDigest

                byte[] data = p.getBytes("UTF-8"); // Get the byte representation of the password string
                byte[] saltData = salt.getBytes("UTF-8"); // Get the byte representation of salt string

                //Update the SHA-256 with these byte arrays
                messageDigest.update(data);
                messageDigest.update(saltData);

                byte[] digest = messageDigest.digest(); //Finally, create the actual data representation of the hash

                //Loop through the bytes and transform them to hexadecimal
                StringBuffer stringBuffer = new StringBuffer();
                for (byte bytes : digest) {
                    stringBuffer.append(String.format("%02x", bytes & 0xff));
                }

                //Return the string of the hexadecimal produced
                return stringBuffer.toString();

            }catch(NoSuchAlgorithmException nsae){} //These need to be caught for formality 
        }catch(UnsupportedEncodingException e){} // but there's no actual way that they will be thrown

        // If for some (very unlikely) reason a catch block is activated, just return the empty string
        return "";
    }

   /**
   * This method takes a string representation of a hexadecimal
   * and translates it into a byte array
   * @param s The actual string of hexadecimal
   * @return byte[], returns the binary representation of the hexadecimal
   */
    public static byte[] hexStringToByteArray(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2]; //Let the byte[] be half the size of the string (since 2 hex chars refer to 1 binary char)
        for (int i = 0; i < len; i += 2) 
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) // Fill the array by converting hex to binary
                                 + Character.digit(s.charAt(i+1), 16));
        }
        //Return the array
        return data;
    }

       /**
   * This method is basically a wrapper for the MessageDigest method isEqual
   * @param d1 An array of bytes (representing a hash)
   * @param d2 Another array of bytes (representing a hash)
   * @return boolean, returns true if d1 == d2 otherwise false
   */
    public static boolean isEqual(byte[] d1, byte[] d2)
    {
        if(MessageDigest.isEqual(d1, d2))
        {
            return true;
        }
        return false;
    }
}