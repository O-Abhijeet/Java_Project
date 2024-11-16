import java.util.Scanner;
import java.time.LocalDate;
import java.time.Period;

public class AgeCalc {

    // Method to check if a given date is valid
    public static boolean validity(int day, int month, int year) {
        if (year <= 0 || month < 1 || month > 12 || day < 1) {
            return false;  // Invalid month or year
        }

        // Handle days in each month
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12:
                return day <= 31;  // Months with 31 days
            case 4, 6, 9, 11:
                return day <= 30;  // Months with 30 days
            case 2:
                // February: Check if the year is a leap year
            if (isLeapYear(year)) {
                return day <= 29;  // Leap year: February has 29 days
            } else {
                return day <= 28;  // Non-leap year: February has 28 days
            }
            default:
                return false;  // Invalid month
        }
    }

    // Method to check if a given year is a leap year
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    // Method to calculate age based on Date of Birth and the date
    public static void calculateAge(int dobDay, int dobMonth, int dobYear, int refDay, int refMonth, int refYear) {
        LocalDate dob = LocalDate.of(dobYear, dobMonth, dobDay);
        LocalDate referenceDate = LocalDate.of(refYear, refMonth, refDay);

        //Calculate the gap between DOB and the date
        Period period = Period.between(dob, referenceDate);

        System.out.println("Age is: " + period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days.");
    }

    // Method to calculate Date of Birth based on age and reference date
    public static void calculateDOB(int ageDay, int ageMonth, int ageYear, int refDay, int refMonth, int refYear) {
        LocalDate referenceDate = LocalDate.of(refYear, refMonth, refDay);
        LocalDate dob = referenceDate.minusYears(ageYear).minusMonths(ageMonth).minusDays(ageDay);

        // Check if the resulting date should be adjusted for leap years
        if (dob.getDayOfMonth() == 29 && dob.getMonthValue() == 2 && !isLeapYear(dob.getYear())) {
            dob = dob.plusDays(1); // Adjust to March 1st in non-leap years
        }

        System.out.println("Date of Birth is: " + dob.getDayOfMonth() + "-" + dob.getMonthValue() + "-" + dob.getYear());
    }

    // Method to parse a date string based on the provided format and delimiter
    public static int[] parseDate(String dateStr, String format, String delimiter) {
        String[] dateParts = dateStr.split(delimiter);
        int day = 0, month = 0, year = 0;

        // Parse date based on the provided format
        if (format.equals("DDdlcMMdlcYYYY")) {
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        } else if (format.equals("YYYYdlcMMdlcDD")) {
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[2]);
        } else if (format.equals("MMdlcDDdlcYYYY")) {
            month = Integer.parseInt(dateParts[0]);
            day = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        }
        return new int[]{day, month, year};//gives the parsed D,M,Y
    }

    // Displaying the instructions for the user
    public static void displayInstructions() {
        System.out.println("Welcome to the Age Calculator!");
        System.out.println("You can calculate either Age from Date of Birth (DOB) or Date of Birth from Age.");
        System.out.println("Please provide the input in the format:");
        System.out.println("For DOB input: DOB=DD-MM-YYYY");
        System.out.println("For AGE input: AGE=DD-MM-YYYY");
        System.out.println("For Date format: DDdlcMMdlcYYYY or YYYYdlcMMdlcDD or MMdlcDDdlcYYYY");
        System.out.println("Delimiter options: - , / , .");
    }

    // Main method: Entry point of the program
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Display instructions
        displayInstructions();

        // Input 1: User provides either DOB or AGE
        System.out.print("Enter the input (e.g., DOB=27-02-2001 or AGE=12-05-0020 or EXIT): ");
        String input = sc.nextLine().trim();

        // Exit condition
        if (input.equalsIgnoreCase("EXIT")) {
            System.out.println("Exiting the program. Thank You and Goodbye!");
            sc.close();
            return;
        }

        // Input 2: Reference or current date
        System.out.print("Enter the reference date or current date (e.g., 27-02-2024): ");
        String refDate = sc.nextLine().trim();

        // Input 3: Date format (e.g., DDdlcMMdlcYYYY, YYYYdlcMMdlcDD, MMdlcDDdlcYYYY)
        System.out.print("Enter the date format (e.g., DDdlcMMdlcYYYY, YYYYdlcMMdlcDD, MMdlcDDdlcYYYY): ");
        String format = sc.nextLine().trim();

        // Input 4: Delimiter used in the date (e.g., -, /)
        System.out.print("Enter the delimiter (e.g., -, /, .): ");
        String delimiter = sc.nextLine().trim();

        // Parse the reference date using the provided format and delimiter
        int[] refDateParts = parseDate(refDate, format, delimiter);
        int refDay = refDateParts[0];
        int refMonth = refDateParts[1];
        int refYear = refDateParts[2];

        // Validate the reference date
        if (!validity(refDay, refMonth, refYear)) {
            System.out.println("Reference date is invalid. Please enter valid details.");
            sc.close();
            return; // Exit if the reference date is invalid
        }

        // Check if the input starts with "DOB" or "AGE"
        if (input.startsWith("DOB")) {
            // If input is DOB, calculate the age
            String dobStr = input.split("=")[1].trim(); // Extract the DOB
            int[] dobParts = parseDate(dobStr, format, delimiter);
            int dobDay = dobParts[0];
            int dobMonth = dobParts[1];
            int dobYear = dobParts[2];

            // Validate the DOB
            if (!validity(dobDay, dobMonth, dobYear)) {
                System.out.println("DOB is invalid. Please enter valid details.");
                sc.close();
                return;
            }
            calculateAge(dobDay, dobMonth, dobYear, refDay, refMonth, refYear); // Calculate the age
        } else if (input.startsWith("AGE")) {
            // If input is AGE, calculate the DOB
            String ageStr = input.split("=")[1].trim();
            int[] ageParts = parseDate(ageStr, format, delimiter);
            int ageDay = ageParts[0];
            int ageMonth = ageParts[1];
            int ageYear = ageParts[2];
            calculateDOB(ageDay, ageMonth, ageYear, refDay, refMonth, refYear); // Calculate the DOB
        } else {
            System.out.println("Invalid input. Please provide either DOB or AGE.");
        }

        sc.close();
    }
}
