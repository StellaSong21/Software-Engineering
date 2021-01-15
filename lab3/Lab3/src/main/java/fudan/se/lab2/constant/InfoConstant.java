package fudan.se.lab2.constant;

public class InfoConstant {
    // framework info
    public static final String ENTITY_EXIST = "{0} already exists, name: {1}";
    public static final String ENTITY_NOT_FOUND = "Object not found, name: {0}";
    public static final String FILE_CANNOT_BE_DIR = "Failed to create file, {0} is not a valid name";
    public static final String CREATING_PARENT_DIR = "Creating parent directory ...";
    public static final String FAILED_CREAT_DIR = "Failed to create target directory";
    public static final String FAILED_TO_CREATE_FILE = "Failed to create file: {0}";
    public static final String SUCCESS_TO_CREATE_FILE = "Success to create file: {0}";
    public static final String FAILED_TO_CREATE_FILE_REASON = "Failed to create file: {0}, because {1}";

    // your info constant
    // user info
    public static final String SUCCESS_TO_SIGN_UP = "User signup successfully, name: {0}";
    public static final String WRONG_MATCH = "Username or password error.";
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String SUCCESS_TO_LOGIN = "User login successfully,name:{0}";
    public static final String USER_LOGIN = "User has logged in";
    public static final String USER_NOT_LOGIN = "Please login";
    public static final String INVALID_NUMBER = "Invalid number of coffees: {0}";
    public static final String USERNAME_VALID = "Username is valid: {0}";
    public static final String INVALID_USERNAME = "Invalid username!";
    public static final String INVALID_PS = "Invalid password!";

    // runtime info
    public static final String NULL_POINTER = "There is a null pointer as the function's parameter";

    //price info
    public static final String ORDER_NULL = "The map of order is null or the order is empty.";
    public static final String ORDER_WRONG = "The order has something wrong.";
    public static final String COFFEE_NAME_WRONG = "Coffee name is wrong.";
    public static final String ORDER_INTEGER_NUM = "The number of coffee is negative or zero.";
    public static final String COFFEE_DEX_WRONG = "Coffee description is wrong.";
    public static final String INVALID_SIZE = "The size of coffee is invalid.";
    public static final String ORDER_INFORMATION = "name: {0}, size: {1}, number: {2}, price: {3}$";
    public static final String CREATE_COFFEE_FAILED = "Failed to create coffee, check csv.";

}
