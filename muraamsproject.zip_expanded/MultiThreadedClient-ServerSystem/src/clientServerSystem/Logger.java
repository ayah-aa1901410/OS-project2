import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Logger { 
    String currentUsersHomeDir = System.getProperty("user.home");
    String path = currentUsersHomeDir+"/Documents/MultiThreadedClient-ServerSystem/src/Log.txt";
    // static variable single_instance of type Singleton 
    private static Logger log_instance = null; 
    private final Lock myLock = new ReentrantLock();
    private FileOutputStream fileOutputStream = null;
    private File logFile;
    // variable of type String 
    //public String s; 
  
    // private constructor restricted to this class itself 
    private Logger() 
    { 
        logFile = new File(path);
    } 
  
    // static method to create instance of Singleton class 
    public static Logger getInstance() 
    { 
        if (log_instance == null) 
            log_instance = new Logger(); 
  
        return log_instance; 
    } 


    public void writeToLog(String event){
        synchronized(this){
            myLock.lock();
            try{
                fileOutputStream = new FileOutputStream(logFile, true);
			    fileOutputStream.write(event.getBytes());;
			    fileOutputStream.flush();
			    fileOutputStream.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                myLock.unlock();
            }
        }
    }
} 