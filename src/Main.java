
// Bu kısım sistemin asıl doğrulama kurallarını içerir.
class RegistrationValidator {

  public boolean validateName(String firstName, String lastName) {
    if (firstName == null || lastName == null) return false;

    // İsim ve soyisim boş olmamalı, 2-30 karakter arası olmalı ve sadece harf içermeli
    String namePattern = "^[a-zA-ZçğıöşüÇĞİÖŞÜ]{2,30}$";
    return firstName.matches(namePattern) && lastName.matches(namePattern);
  }

  public boolean validateEmail(String email) {
    if (email == null) return false;
    // Standart email formatı kontrolü
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailPattern) && email.contains(".");
  }

  public boolean validatePassword(String password, String confirmPassword) {
    if (password == null || confirmPassword == null) return false;

    // Şifre en az 8 karakter olmalı ve onay şifresiyle eşleşmeli
    return password.length() >= 8 && password.equals(confirmPassword);
  }

  public boolean validateAge(String dob) {
    if (dob == null || !dob.matches("\\d{2}/\\d{2}/\\d{4}")) return false;

    try {
      String[] dateParts = dob.split("/");
      int day = Integer.parseInt(dateParts[0]);
      int month = Integer.parseInt(dateParts[1]);
      int year = Integer.parseInt(dateParts[2]);

      // Boundary Check: Gün ve Ay sınırları
      if (day < 1 || day > 31 || month < 1 || month > 12) return false;

      // Age Check: 2026 yılına göre minimum 18 yaş sınırı
      int currentYear = 2026;
      return (currentYear - year) >= 18;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}

// --- SECTION 2: TEST ENGINE ---
// Testleri koşturan ve raporlayan ana sınıf.
public class Main {
  private static RegistrationValidator validator;
  private static int testsPassed = 0;
  private static int testsFailed = 0;

  // Test suite başlamadan önce hazırlık yapar.
  public static void initializeSuite() {
    validator = new RegistrationValidator();
    System.out.println("==========================================");
    System.out.println("LOG: Unit Test Suite Initialization...");
    System.out.println("==========================================\n");
  }

  public static void main(String[] args) {
    initializeSuite();

    // --- NAME VALIDATION TESTS (TC01 - TC04) ---
    executeTest(validator.validateName("Esengul", "Velet"), true, "TC01: Standard Valid Name");
    executeTest(validator.validateName("", "Ogan"), false, "TC02: Empty Name Field");
    executeTest(validator.validateName("E", "Velet"), false, "TC03: Name Below Length Limit");
    executeTest(validator.validateName("Eren123", "Ogan"), false, "TC04: Name Containing Digits");

    // --- EMAIL VALIDATION TESTS (TC05 - TC07) ---
    executeTest(validator.validateEmail("test@kalyoncu.edu.tr"), true, "TC05: Valid Corporate Email");
    executeTest(validator.validateEmail("invalid-email.com"), false, "TC06: Missing '@' Symbol");
    executeTest(validator.validateEmail("test@domain"), false, "TC07: Missing Domain Extension");

    // --- DATE OF BIRTH TESTS (TC08 - TC11) ---
    executeTest(validator.validateAge("15/05/2000"), true, "TC08: Valid Adult User");
    executeTest(validator.validateAge("2000-05-15"), false, "TC09: Invalid Date Format");
    executeTest(validator.validateAge("32/05/2000"), false, "TC10: Invalid Day Boundary");
    executeTest(validator.validateAge("04/05/2009"), false, "TC11: Underage User Check");

    // --- PASSWORD SECURITY TESTS (TC12 - TC15) ---
    executeTest(validator.validatePassword("Pass1234", "Pass1234"), true, "TC12: Matching Passwords");
    executeTest(validator.validatePassword("Pass1234", "Diff5678"), false, "TC13: Password Mismatch");
    executeTest(validator.validatePassword("12345678", "12345678"), true, "TC14: Exact 8-Char Boundary");
    executeTest(validator.validatePassword("1234567", "1234567"), false, "TC15: 7-Char Below Boundary");

    // --- INTEGRATION TEST (TC16) ---
    boolean isFormValid = validator.validateName("Eren", "Ogan") &&
            validator.validateEmail("eren@ogan.com") &&
            validator.validateAge("01/01/2000") &&
            validator.validatePassword("Admin123", "Admin123");
    executeTest(isFormValid, true, "TC16: Full Form Submission Integration");

    printFinalReport();
  }

  // Assertion logic: Beklenen ve gerçek sonuçları karşılaştırır.
  private static void executeTest(boolean actual, boolean expected, String testName) {
    if (actual == expected) {
      System.out.printf("[PASSED] %s%n", testName);
      testsPassed++;
    } else {
      System.out.printf("[FAILED] %s%n", testName);
      testsFailed++;
    }
  }

  private static void printFinalReport() {
    System.out.println("\n------------------------------------------");
    System.out.println("EXECUTION SUMMARY");
    System.out.println("Total Passed: " + testsPassed);
    System.out.println("Total Failed: " + testsFailed);
    System.out.println("Pass Rate: %" + (testsPassed * 100 / (testsPassed + testsFailed)));
    System.out.println("------------------------------------------");
  }}
