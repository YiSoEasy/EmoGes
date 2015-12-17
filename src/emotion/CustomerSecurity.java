package emotion;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Emotiv EPOC
 *
 */
public interface CustomerSecurity extends Library  
{
	CustomerSecurity INSTANCE = (CustomerSecurity)
            Native.loadLibrary("CustomerSecurity",
            		CustomerSecurity.class);
    	
	double emotiv_func(double x);
}