// --- SECTION 1: VALIDATION LOGIC ---
class RegistrationValidator {

  public boolean validateName(String firstName, String lastName) {
    if (firstName == null || lastName == null) return false;
    String namePattern = "^[a-zA-ZçğıöşüÇĞİÖŞÜ]{2,30}$";
    return firstName.matches(namePattern) && lastName.matches(namePattern);
  }

  public boolean validateEmail(String email) {
    if (email == null) return false;
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailPattern) && email.contains(".");
  }

  public boolean validatePassword(String password, String confirmPassword) {
    if (password == null || confirmPassword == null) return false;
    return password.length() >= 8 && password.equals(confirmPassword);
  }

  public boolean validateAge(String dob) {
    if (dob == null || !dob.matches("\\d{2}/\\d{2}/\\d{4}")) return false;
    try {
      String[] dateParts = dob.split("/");
      int day = Integer.parseInt(dateParts[0]);
      int month = Integer.parseInt(dateParts[1]);
      int year = Integer.parseInt(dateParts[2]);

      if (day < 1 || day > 31 || month < 1 || month > 12) return false;

      int currentYear = 2026;
      return (currentYear - year) >= 18;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}

// --- SECTION 2: CUSTOM ASSERTIONS FRAMEWORK ---
// Hocanın istediği "Assertions" yapısı.
class Assertions {
  public static void assertTrue(boolean condition, String testName) {
    if (condition) {
      System.out.printf("[PASSED] %s%n", testName);
      Main.testsPassed++;
    } else {
      System.out.printf("[FAILED] %s%n", testName);
      Main.testsFailed++;
    }
  }

  public static void assertFalse(boolean condition, String testName) {
    assertTrue(!condition, testName); // assertFalse, durumun false olmasını bekler
  }
}

// --- SECTION 3: TEST ENGINE ---
public class Main {
  private static RegistrationValidator validator;
  public static int testsPassed = 0;
  public static int testsFailed = 0;

  // --- SETUP METHOD ---
  // Hocanın İstediği: Her testten önce ortamı hazırlar
  public static void setUp() {
    validator = new RegistrationValidator();
  }

  // --- TEARDOWN METHOD ---
  // Hocanın İstediği: Her testten sonra verileri/objeleri temizler
  public static void tearDown() {
    validator = null;
  }

  // Ana çalıştırıcı (Test Runner)
  public static void main(String[] args) {
    System.out.println("==========================================");
    System.out.println("LOG: Starting Unit Test Execution...");
    System.out.println("==========================================\n");

    // --- NAME VALIDATION TESTS (TC01 - TC04) ---
    setUp();
    Assertions.assertTrue(validator.validateName("Esengul", "Velet"), "TC01 [EP]: Valid Standard Name");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateName("", "Ogan"), "TC02 [BVA]: Empty Name Field (Length 0)");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateName("E", "Velet"), "TC03 [BVA]: Name Below Min Length Limit (1 Char)");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateName("Eren123", "Ogan"), "TC04 [EP]: Invalid Name Containing Digits");
    tearDown();

    // --- EMAIL VALIDATION TESTS (TC05 - TC07) ---
    setUp();
    Assertions.assertTrue(validator.validateEmail("test@kalyoncu.edu.tr"), "TC05 [EP]: Valid Corporate Email Format");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateEmail("invalid-email.com"), "TC06 [EP]: Invalid Email Missing '@' Symbol");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateEmail("test@domain"), "TC07 [EP]: Invalid Email Missing Domain Extension");
    tearDown();

    // --- DATE OF BIRTH TESTS (TC08 - TC11) ---
    setUp();
    Assertions.assertTrue(validator.validateAge("15/05/2000"), "TC08 [EP]: Valid Adult User Check");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateAge("2000-05-15"), "TC09 [EP]: Invalid Date Format Provided");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateAge("32/05/2000"), "TC10 [BVA]: Invalid Day Boundary Exceeded (32)");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validateAge("04/05/2009"), "TC11 [BVA]: Underage User Check (Under 18)");
    tearDown();

    // --- PASSWORD SECURITY TESTS (TC12 - TC15) ---
    setUp();
    Assertions.assertTrue(validator.validatePassword("Pass1234", "Pass1234"), "TC12 [EP]: Valid Matching Passwords");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validatePassword("Pass1234", "Diff5678"), "TC13 [EP]: Password and Confirm Password Mismatch");
    tearDown();

    setUp();
    Assertions.assertTrue(validator.validatePassword("12345678", "12345678"), "TC14 [BVA]: Exact 8-Char Password Boundary Validated");
    tearDown();

    setUp();
    Assertions.assertFalse(validator.validatePassword("1234567", "1234567"), "TC15 [BVA]: 7-Char Password Below Minimum Boundary");
    tearDown();

    // --- INTEGRATION TEST (TC16) ---
    setUp();
    boolean isFormValid = validator.validateName("Eren", "Ogan") &&
            validator.validateEmail("eren@ogan.com") &&
            validator.validateAge("01/01/2000") &&
            validator.validatePassword("Admin123", "Admin123");
    Assertions.assertTrue(isFormValid, "TC16: Full Form Submission Integration Test");
    tearDown();

    printFinalReport();
  }

  private static void printFinalReport() {
    System.out.println("\n------------------------------------------");
    System.out.println("TEST EXECUTION SUMMARY");
    System.out.println("Total Passed: " + testsPassed);
    System.out.println("Total Failed: " + testsFailed);
    System.out.println("Pass Rate: %" + (testsPassed * 100 / (testsPassed + testsFailed)));
    System.out.println("------------------------------------------");
  }}
