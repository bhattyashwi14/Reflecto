package Functionalities;

import java.sql.*;
import java.util.*;
import DS.*;
public class Challenges extends LoginSignup {

    private final Scanner sc = new Scanner(System.in);
    private final Connection conn = super.con;


    public static class ChallengeRecord {
        int cID;
        int userID;
        String challengeDesc;
        String accepted;
        String accomplished;

        ChallengeRecord(int cID, int userID, String challengeDesc, String accepted, String accomplished) {
            this.cID = cID;
            this.userID =userID;
            this.challengeDesc = challengeDesc;
            this.accepted = accepted;
            this.accomplished = accomplished;
        }

        @Override
        public String toString() {
            return "Challenge ID: " + cID +
                    " | User ID: " + userID +
                    " | Desc: " + challengeDesc +
                    " | Accepted: " + accepted +
                    " | Accomplished: " + accomplished;
        }
    }




    static int choice1;

    @Override
    public void displayMenu() //Displays the menu for Challenges (overridden method)
    {
        System.out.println();
        System.out.println("\u001B[36mCHALLENGES:\u001B[0m");
        System.out.println("\u001B[33m1. BROWSE ALL CHALLENGES\u001B[0m");
        System.out.println("\u001B[33m2. ADD A NEW CHALLENGE\u001B[0m");
        System.out.println("\u001B[33m3. MY CHALLENGES\u001B[0m");
        System.out.println("\u001B[33m4. MY ACCOMPLISHMENTS\u001B[0m");
        System.out.println("\u001B[33m5. BACK\u001B[0m");

        while (true) {

            try {
                System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                choice1 = sc.nextInt();
                sc.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                sc.nextLine();
            }

        }
    }

    public void challengesMenu(LoginSignup currentUser) {
        boolean exit = false;
        while (!exit) {
            displayMenu();
            switch (choice1) {
                case 1:
                    browseAllChallenges();
                    break;
                case 2:
                    addChallenge();
                    break;
                case 3:
                    myChallengesSection();
                    break;
                case 4:
                    browseAccomplishments();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
            }
        }
    }


    // ===== STACK OF ALL CHALLENGES =====
    private ChallengesStack createChallengesStack(int size) {

        ChallengesStack st = new ChallengesStack(size);

        String q1 = "{call getAllChallenges()}";

        try {

            CallableStatement cst = conn.prepareCall(q1);
            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                st.push(new ChallengeRecord(
                        rs.getInt(2),  // challenge_ID
                        rs.getInt(1),  // user_id
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error (createAllChallengesStack): " + e.getMessage() + "\u001B[0m");
        }

        return st;
    }

    // ===== COUNT OF ALL CHALLENGES =====
    private int totalChallenges() {

        String q1 = "{call getChallengesCount()}";

        try {

            CallableStatement cst = conn.prepareCall(q1);
            ResultSet rs = cst.executeQuery();

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            System.out.println("\u001B[31mCould not retrieve challenges count." + e.getMessage() + "\u001B[0m");
            return 0;
        }

    }

    // ===== BROWSE ALL CHALLENGES =====
    public void browseAllChallenges(){

        int count = totalChallenges();

        if (count == 0) {
            System.out.println("\u001B[31mNo challenges found.\u001B[0m");
            return;
        }

        ChallengesStack st = createChallengesStack(count);

        int idx = 1;
        boolean exit = false;

        while (!exit) {

            ChallengeRecord curr = st.peep(idx);

            System.out.println();
            System.out.println("\u001B[36mChallenge Description:\u001B[0m " + curr.challengeDesc);
            System.out.println("\u001B[33mAccepted:\u001B[0m " + curr.accepted);
            System.out.println("\u001B[32mAccomplished:\u001B[0m " + curr.accomplished);
            System.out.println();
            System.out.println("\u001B[33m1. PREV\u001B[0m");
            System.out.println("\u001B[33m2. ACCEPT CHALLENGE\u001B[0m");
            System.out.println("\u001B[33m3. NEXT\u001B[0m");
            System.out.println("\u001B[33m4. RANDOM\u001B[0m");
            System.out.println("\u001B[33m5. BACK\u001B[0m");
            System.out.println();

            int choice;

            while (true) {

                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }

            }

            switch (choice) {
                case 1 :

                    if(idx == 1)
                        idx = count;
                    else
                        idx = idx - 1;

                    break;

                case 2 :

                    acceptChallengeIfPossible(curr);

                    break;
                case 3 :

                    if(idx == count)
                        idx = 1;
                    else
                        idx = idx + 1;

                    break;
                case 4 :

                    idx = (int) ((Math.random() * count) + 1);

                    break;
                case 5 :

                    exit = true;

                    break;
                default :
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
            }
        }

    }

    // ===== ACCEPT CHALLENGE =====
    private void acceptChallengeIfPossible(ChallengeRecord rec) {

        System.out.println();

        if ("No".equalsIgnoreCase(rec.accepted)) {

            String q = "UPDATE challenges_table SET accepted='Yes' WHERE user_ID=? AND challenge_ID=?";

            try  {

                PreparedStatement pst = conn.prepareStatement(q);

                pst.setInt(1,rec.userID);
                pst.setInt(2, rec.cID);

                int r = pst.executeUpdate();

                if (r > 0) {

                    rec.accepted = "Yes";
                    System.out.println("\u001B[32mCHALLENGE ACCEPTED.\u001B[0m");

                } else {

                    System.out.println("\u001B[31mCOULD NOT ACCEPT THE CHALLENGE. PLEASE TRY AGAIN.\u001B[0m");

                }
            }catch (SQLException e) {
                System.out.println("\u001B[31mDB Error (acceptChallengeIfPossible): " + e.getMessage() + "\u001B[0m");
            }

        } else if ("Yes".equalsIgnoreCase(rec.accepted) && !("Done".equalsIgnoreCase(rec.accomplished))) {
            System.out.println("\u001B[33mCHALLENGE ALREADY IN PROGRESS.\u001B[0m");
        } else if ("Done".equalsIgnoreCase(rec.accepted)) {
            System.out.println("\u001B[36mALREADY ACCOMPLISHED.\u001B[0m");
        } else {
            System.out.println("\u001B[31mUNKNOWN STATUS FOR CHALLENGE.\u001B[0m");
        }
    }



    // ===== ADD CHALLENGE =====
    public void addChallenge() {

        System.out.println("\u001B[36mEnter the challenge description:\u001B[0m");
        String challengeDescription = sc.nextLine();

        if (challengeDescription.isBlank()) {
            System.out.println("\u001B[31mChallenge description cannot be empty!\u001B[0m");
            return;
        }

        String q1 = "{call addNewChallenge(?,?)}";

        try {

            CallableStatement cst1 = conn.prepareCall(q1);

            cst1.setInt(1, userId);
            cst1.setString(2, challengeDescription);

            int r = cst1.executeUpdate();
            if (r > 0) {
                System.out.println("\u001B[32mCHALLENGE ADDED SUCCESSFULLY.\u001B[0m");
            } else {
                System.out.println("\u001B[31mPLEASE TRY AGAIN.\u001B[0m");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (addChallenge): " + e.getMessage() + "\u001B[0m");
        }
    }



    // ===== USER'S TOTAL POSTED CHALLENGES =====
    private int userIdTotalChallenges(int userID) {

        String q1 = "{call getChallengesCountByUserID(?)}";

        try {

            CallableStatement cst = conn.prepareCall(q1);
            cst.setInt(1, userID);
            try (ResultSet rs = cst.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (userIdTotalChallenges): " + e.getMessage() + "\u001B[0m");
            return 0;
        }

    }

    // ===== CREATE CHALLENGES STACK =====
    private ChallengesStack createUserIdChallengesStack(int userID, int size) {

        ChallengesStack st = new ChallengesStack(size);

        String q1 = "{call getAllChallengesByUserID(?)}";
        try  {

            CallableStatement cst = conn.prepareCall(q1);
            cst.setInt(1, userID);
            try (ResultSet rs = cst.executeQuery()) {
                while (rs.next()) {
                    st.push(new ChallengeRecord(
                            rs.getInt(2),  // challenge_ID
                            rs.getInt(1),  // user_id
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (createUserIdChallengesStack): " + e.getMessage() + "\u001B[0m");
        }
        return st;
    }



    // ===== BROWSE CHALLENGES =====
    private void browseMyChallenges()
    {

        int count = userIdTotalChallenges(userId);

        if (count == 0) {
            System.out.println("\u001B[33mNo challenges found for this user.\u001B[0m");
            return;
        }

        ChallengesStack st = createUserIdChallengesStack(userId, count);

        int idx = 1;
        boolean exit = false;

        while (!exit) {
            ChallengeRecord curr = st.peep(idx);
            System.out.println();
            System.out.println("\u001B[36mChallenge Description:\u001B[0m " + curr.challengeDesc);
            System.out.println("\u001B[36mAccepted:\u001B[0m " + curr.accepted);
            System.out.println("\u001B[36mAccomplished:\u001B[0m " + curr.accomplished);
            System.out.println();
            System.out.println("\u001B[33m1. PREV\u001B[0m");
            System.out.println("\u001B[33m2. ACCEPT CHALLENGE\u001B[0m");
            System.out.println("\u001B[33m3. NEXT\u001B[0m");
            System.out.println("\u001B[33m4. RANDOM\u001B[0m");
            System.out.println("\u001B[33m5. BACK\u001B[0m");
            System.out.println();

            int choice;

            while (true) {

                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }

            }

            switch (choice) {
                case 1:
                    if (idx == 1)
                        idx = count;
                    else
                        idx = idx - 1;
                    break;

                case 2:
                    acceptChallengeIfPossible(curr);
                    break;
                case 3:
                    if (idx == count)
                        idx = 1;
                    else
                        idx = idx + 1;
                    break;
                case 4:
                    idx = (int) ((Math.random() * count) + 1);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
            }
        }
    }

    // ===== MY CHALLENGES =====
    public void myChallengesSection() {

        int count = userIdTotalChallenges(userId);

        if (count == 0) {
            System.out.println("\u001B[31mYOU HAVE POSTED NO CHALLENGES.\u001B[0m");
            return;
        }

        ChallengesStack myChallenges = createChallengesStack(count);

        ChallengeRecord temp;

        System.out.println("\u001B[36mYOUR CHALLENGES:\u001B[0m");
        for (int i = 1; i <= count; i++) {
            temp = myChallenges.peep(i);
            System.out.println(temp);
        }

        boolean exit = false;

        while (!exit) {

            System.out.println();
            System.out.println("\u001B[36mWHAT COURSE OF ACTION DO YOU PREFER?\u001B[0m");
            System.out.println("\u001B[33m1. DELETE A CHALLENGE\u001B[0m");
            System.out.println("\u001B[33m2. MARK A CHALLENGE AS COMPLETED\u001B[0m");
            System.out.println("\u001B[33m3. BROWSE THROUGH MY CHALLENGES\u001B[0m");
            System.out.println("\u001B[33m4. EXIT\u001B[0m");

            int choice;

            while (true) {

                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }

            }

            switch (choice) {
                case 1:

                    System.out.println("\u001B[36mYOUR CHALLENGES:\u001B[0m");
                    for (int i = 1; i <= count; i++) {
                        temp = myChallenges.peep(i);
                        System.out.println(temp);
                    }


                    int delID;

                    while (true) {
                        try {
                            System.out.print("\u001B[35mENTER THE CHALLENGE ID YOU WANT TO DELETE:\u001B[0m");
                            delID = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (Exception e) {
                            System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                            sc.nextLine();
                        }

                    }

                    deleteMyChallenge(delID);

                    break;

                case 2:

                    System.out.println("\u001B[36mYOUR CHALLENGES:\u001B[0m");
                    for (int i = 1; i <= count; i++) {
                        temp = myChallenges.peep(i);
                        System.out.println(temp);
                    }


                    int accID;

                    while (true) {
                        try {
                            System.out.print("\u001B[35mENTER THE CHALLENGE ID YOU WANT TO MARK AS ACCOMPLISHED:\u001B[0m");
                            accID = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (Exception e) {
                            System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                            sc.nextLine();
                        }

                    }

                    markChallengeAsCompleted(accID);

                    break;

                case 3:

                    browseMyChallenges();

                    break;

                case 4:

                    exit = true;

                    break;
                default:
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
            }

        }
    }

    // ===== DELETE CHALLENGE =====
    private void deleteMyChallenge(int delID) {

        String q1 = "{call delChallenge(?,?)}";

        try  {
            CallableStatement cst1 = conn.prepareCall(q1);
            cst1.setInt(1, delID);
            cst1.setInt(2, userId);

            int r = cst1.executeUpdate();
            if (r > 0) {
                System.out.println("\u001B[32mDELETION SUCCESSFUL.\u001B[0m");
            } else {
                System.out.println("\u001B[31mDELETION FAILED. PLEASE TRY AGAIN.\u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (deleteMyChallenge): " + e.getMessage() + "\u001B[0m");
        }
    }

    // ===== MARK AS ACCOMPLISH =====
    private void markChallengeAsCompleted(int accomplishedID) {

        String q1 = "{call markAsAccomplished(?,?)}";

        try  {
            CallableStatement cst1 = conn.prepareCall(q1);
            cst1.setInt(1, accomplishedID);
            cst1.setInt(2,userId);

            int r = cst1.executeUpdate();
            if (r > 0) {
                System.out.println("\u001B[32mCONGRATULATIONS!!!\u001B[0m");
                System.out.println("\u001B[32mCHALLENGE ACCOMPLISHED.\u001B[0m");
            } else {
                System.out.println("\u001B[31mUPDATE FAILED. PLEASE TRY AGAIN.\u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (markChallengeAsCompleted): " + e.getMessage() + "\u001B[0m");
        }
    }



    // ===== ACCOMPLISHMENTS STACK =====
    private ChallengesStack makeAccStack(int size){

        ChallengesStack st = new ChallengesStack(size);

        String q1 = "{call getAccomplishmentsByUserID(?)}";

        try  {

            CallableStatement cst = conn.prepareCall(q1);
            cst.setInt(1, userId);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                st.push(new ChallengeRecord(
                        rs.getInt(2),  // challenge_ID
                        rs.getInt(1),  // user_id
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (createUserIdChallengesStack): " + e.getMessage() + "\u001B[0m");
        }
        return st;

    }

    // ===== ACCOMPLISHMENTS COUNT =====
    private int userIdTotalAccomplishments(int userID) {

        String q1 = "{call getAccomplishmentsCountByUserID(?)}";

        try {
            CallableStatement cst1 = conn.prepareCall(q1);
            cst1.setInt(1, userID);
            ResultSet rs1 = cst1.executeQuery();

            return rs1.next() ? rs1.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (userIdTotalAccomplishments): " + e.getMessage() + "\u001B[0m");
            return 0;
        }
    }

    // ==== BROWSE ACCOMPLISHMENTS =====
    public void browseAccomplishments() {

        int count = userIdTotalAccomplishments(userId);

        if (count == 0) {
            System.out.println("\u001B[33mNo challenges found for this user.\u001B[0m");
            return;
        }

        ChallengesStack st = makeAccStack(count);

        for(int i = 1; i <= count; i++){
            ChallengeRecord curr = st.peep(i);
            System.out.println(curr);
        }

        boolean exit = false;

        while (!exit) {

            System.out.println();
            System.out.println("\u001B[36mWHAT COURSE OF ACTION DO YOU PREFER?\u001B[0m");
            System.out.println("\u001B[33m1. MARK AN ACCOMPLISHMENT AS UNACCOMPLISHED\u001B[0m");
            System.out.println("\u001B[33m2. BROWSE THROUGH MY ACCOMPLISHMENTS\u001B[0m");
            System.out.println("\u001B[33m3. EXIT\u001B[0m");

            int choice;

            while (true) {

                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }

            }


            switch (choice) {
                case 1 :
                    int unAccID;
                    while (true) {

                        try {
                            System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                            unAccID = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (Exception e) {
                            System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                            sc.nextLine();
                        }

                    }
                    unAccomplishChallenge(unAccID);

                    break;

                case 2 :
                    browseAccomplishmentsByUserID(userId);
                    break;

                case 3 :
                    exit = true;
                    break;
                default :
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
            }
        }
    }

    // ===== UN-ACCOMPLISH =====
    private void unAccomplishChallenge(int unAccomplishID) {

        String q1 = "{call markAsUnAccomplished(?,?)}";

        try {
            CallableStatement cst1 = conn.prepareCall(q1);
            cst1.setInt(1, unAccomplishID);
            cst1.setInt(2, userId);

            int r = cst1.executeUpdate();
            if (r > 0) {
                System.out.println("\u001B[32mUN-ACCOMPLISHED\u001B[0m");
            } else {
                System.out.println("\u001B[31mPLEASE TRY AGAIN\u001B[0m");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (unAccomplishChallenge): " + e.getMessage() + "\u001B[0m");
        }
    }

    // ===== CREATE ACCOMPLISHMENTS STACK =====

    /**
     *
     * @param userID
     * @param size
     * @return
     */
    private ChallengesStack createUserIdAccomplishmentsStack(int userID, int size) {

        ChallengesStack st = new ChallengesStack(size);

        String q1 = "{call getAccomplishmentsByUserID(?)}";
        try {
            CallableStatement cst1 = conn.prepareCall(q1);
            cst1.setInt(1, userID);
            try (ResultSet rs1 = cst1.executeQuery()) {
                while (rs1.next()) {
                    st.push(new ChallengeRecord(
                            rs1.getInt(2),  // challenge_ID
                            rs1.getInt(1),  // user_id
                            rs1.getString(3),
                            rs1.getString(4),
                            rs1.getString(5)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDB Error (createUserIdAccomplishmentsStack): " + e.getMessage() + "\u001B[0m");
        }
        return st;
    }

    // ===== BROWSE ACCOMPLISHMENTS =====
    private void browseAccomplishmentsByUserID(int userID) {
        try {
            int count = userIdTotalAccomplishments(userID);

            if (count == 0) {
                System.out.println("\u001B[33m⚠ No accomplishments found.\u001B[0m");
                return;
            }

            ChallengesStack st = createUserIdAccomplishmentsStack(userID,count);

            int idx = 1;
            boolean exit = false;

            while (!exit) {

                ChallengeRecord cur = st.peep(idx);

                System.out.println("\n\u001B[36m" + cur.challengeDesc + "\u001B[0m");
                System.out.println("Accomplished: \u001B[33m" + cur.accomplished + "\u001B[0m");
                System.out.println("1. PREV");
                System.out.println("2. UN-ACCOMPLISH");
                System.out.println("3. NEXT");
                System.out.println("4. RANDOM");
                System.out.println("5. BACK");

                int choice;

                while (true) {

                    try {
                        System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                        choice = sc.nextInt();
                        sc.nextLine();
                        break;
                    } catch (Exception e) {
                        System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                        sc.nextLine();
                    }

                }

                switch (choice) {
                    case 1 :
                        if (idx == 1)
                            idx = count;
                        else
                            idx = idx - 1;
                        break;

                    case 2 :

                        if ("Yes".equalsIgnoreCase(cur.accomplished)) {
                            String q = "UPDATE challenges_table SET accomplished='No' WHERE user_ID=? AND challenge_ID=?";
                            try {
                                PreparedStatement pst = conn.prepareStatement(q);
                                pst.setInt(1,userId);
                                pst.setInt(2, cur.cID);
                                int r = pst.executeUpdate();
                                if (r > 0) {
                                    cur.accomplished = "No";
                                    System.out.println("\u001B[32mUN-ACCOMPLISHED\u001B[0m");
                                } else {
                                    System.out.println("\u001B[31mPLEASE TRY AGAIN.\u001B[0m");
                                }
                            } catch (SQLException e) {
                                System.out.println("\u001B[31mDB Error (UN-ACCOMPLISH): " + e.getMessage() + "\u001B[0m");
                            }
                        } else {
                            System.out.println("\u001B[33mThis challenge has not been accomplished yet.\u001B[0m");
                        }

                        break;

                    case 3 :
                        if (idx == count)
                            idx = 1;
                        else
                            idx = idx + 1;
                        break;

                    case 4 :
                        idx = (int) ((Math.random() * count) + 1);
                        break;

                    case 5 :
                        exit = true;
                        break;

                    default :
                        System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
                }
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mUnexpected error in browseAccomplishmentsByUserID: " + e.getMessage() + "\u001B[0m");
        }
    }

}

