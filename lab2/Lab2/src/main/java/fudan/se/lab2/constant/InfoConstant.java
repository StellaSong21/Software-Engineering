package fudan.se.lab2.constant;

public class InfoConstant {
    // framework info
    public static final String Entity_EXIST = "{0} already exists, name: {1}";
    public static final String Entity_NOT_FOUND = "Object not found, name: {0}";
    public static final String FILE_CANNOT_BE_DIR = "Failed to create file, {0} is not a valid name";
    public static final String CREATING_PARENT_DIR = "Creating parent directory ...";
    public static final String FAILED_CREAT_DIR = "Failed to create target directory";
    public static final String FAILED_TO_CREATE_FILE = "Failed to create file: {0}";
    public static final String SUCCESS_TO_CREATE_FILE = "Success to create file: {0}";
    public static final String FAILED_TO_CREATE_FILE_REASON = "Failed to create file: {0}, because {1}";

    // your info constant
    public static final String USERNAME_EMPTY = "User name cannot be empty";
    public static final String SUCCESS_TO_SIGN_UP = "User login successfully, name: {0}";
    //wrong password
    public static final String WRONG_MATCH = "Username or password error.";
    public static final String USER_NOT_FOUND = "User not found, name: {0}";
    public static final String NULL_POINTER = "There is a null pointer as the function's parameter";
    public static final String SUCCESS_TO_LOGIN = "User login successfully,name:{0}";
    public static final String PASS_EMPTY = "Password cannot be empty";
    public static final String USER_LOGIN = "User has logged in";
    public static final String USER_NOT_LOGIN = "please login";
    public static final String USER_CSV_ERROR = "User.csv has a wrong item whose name is {0}";
    public static final String INVALID_NUMBER = "Invalid number:{0}";


    //price info
    public static final String ORDER_NEGATIVE_NUM = "The number of coffee is negative.";
    public static final String INVALID_SIZE = "The size of coffee is invalid. Use default size.";
    public static final String ORDER_INFORMATION = "name: {0}, size: {1}, number: {2}, price: ${3}";

}
