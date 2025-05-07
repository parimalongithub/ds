import java.util.*;

class Process {
    int id;
    String state;
    int index;
    boolean hasSentMessage;
}

public class ElectionAlgorithms {

    // Bully algorithm variables
    static boolean[] bullyState = new boolean[5]; // true = up, false = down
    static int coordinator = 5;

    // ---------------- BULLY ALGORITHM METHODS ---------------- //

    public static void bullyUp(int up) {
        if (bullyState[up - 1]) {
            System.out.println("Process " + up + " is already up.");
        } else {
            bullyState[up - 1] = true;
            System.out.println("Process " + up + " is now up.");
            bullyElection(up);
        }
    }

    public static void bullyDown(int down) {
        if (!bullyState[down - 1]) {
            System.out.println("Process " + down + " is already down.");
        } else {
            bullyState[down - 1] = false;
            System.out.println("Process " + down + " is down.");
            if (down == coordinator) {
                System.out.println("Coordinator (Process " + down + ") is down.");
                for (int i = 4; i >= 0; i--) {
                    if (bullyState[i]) {
                        bullyElection(i + 1);
                        break;
                    }
                }
            }
        }
    }

    public static void bullyMess(int mess) {
        if (!bullyState[mess - 1]) {
            System.out.println("Process " + mess + " is down.");
        } else {
            if (mess == coordinator) {
                System.out.println("Coordinator (Process " + coordinator + ") received the message: OK");
            } else {
                System.out.println("Process " + mess + " sends a message.");
                if (!bullyState[coordinator - 1]) {
                    bullyElection(mess);
                }
            }
        }
    }

    public static void bullyElection(int initiator) {
        System.out.println("Election initiated by Process " + initiator);
        boolean foundHigher = false;
        for (int i = initiator + 1; i <= 5; i++) {
            if (bullyState[i - 1]) {
                foundHigher = true;
                System.out.println("Election message sent from Process " + initiator + " to Process " + i);
            }
        }

        if (!foundHigher) {
            coordinator = initiator;
            System.out.println("Process " + coordinator + " becomes the new coordinator.");
            System.out.println("Coordinator message sent from Process " + coordinator + " to all.");
        }
    }

    // ---------------- RING ALGORITHM METHODS ---------------- //
    public static void ringElection(Process[] processes, int initiatorIndex, int numProcesses) {
        System.out.println("Election initiated by Process " + processes[initiatorIndex].id);
    
        int currentIndex = (initiatorIndex + 1) % numProcesses;
        int maxId = processes[initiatorIndex].id;
    
        while (currentIndex != initiatorIndex) {
            if (processes[currentIndex].state.equals("active")) {
                System.out.println("Process " + processes[(currentIndex - 1 + numProcesses) % numProcesses].id +
                                   " sends message to Process " + processes[currentIndex].id);
                if (processes[currentIndex].id > maxId) {
                    maxId = processes[currentIndex].id;
                }
            }
            currentIndex = (currentIndex + 1) % numProcesses;
        }
    
        System.out.println("Election message returns to initiator.");
        System.out.println("Process " + maxId + " becomes the coordinator.");
    }
    

    // ---------------- MAIN PROGRAM ---------------- //

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose Election Algorithm:");
        System.out.println("1. Bully Algorithm");
        System.out.println("2. Ring Algorithm");
        int algoChoice = sc.nextInt();

        switch (algoChoice) {
            case 1: {
                int choice;
                for (int i = 0; i < 5; ++i) bullyState[i] = true;

                System.out.println("5 active processes are: P1 P2 P3 P4 P5");
                System.out.println("Process 5 is the initial coordinator");

                do {
                    System.out.println("\n1. Bring a process UP");
                    System.out.println("2. Bring a process DOWN");
                    System.out.println("3. Send a message");
                    System.out.println("4. Exit");
                    choice = sc.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter process number to UP (1-5): ");
                            int up = sc.nextInt();
                            if (up > 5 || up < 1) System.out.println("Invalid process number.");
                            else bullyUp(up);
                            break;
                        case 2:
                            System.out.print("Enter process number to DOWN (1-5): ");
                            int down = sc.nextInt();
                            if (down > 5 || down < 1) System.out.println("Invalid process number.");
                            else bullyDown(down);
                            break;
                        case 3:
                            System.out.print("Which process will send message (1-5): ");
                            int mess = sc.nextInt();
                            if (mess > 5 || mess < 1) System.out.println("Invalid process number.");
                            else bullyMess(mess);
                            break;
                        case 4:
                            System.out.println("Exiting Bully Algorithm.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                } while (choice != 4);
                break;
            }

            case 2: {
                System.out.print("Enter number of processes: ");
                int n = sc.nextInt();
                Process[] processes = new Process[n];

                for (int i = 0; i < n; i++) {
                    processes[i] = new Process();
                    processes[i].index = i;
                    System.out.print("Enter ID of process " + (i + 1) + ": ");
                    processes[i].id = sc.nextInt();
                    processes[i].state = "active";
                    processes[i].hasSentMessage = false;
                }

                // Sort by process ID
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        if (processes[j].id > processes[j + 1].id) {
                            Process temp = processes[j];
                            processes[j] = processes[j + 1];
                            processes[j + 1] = temp;
                        }
                    }
                }

                System.out.println("\nProcesses in sorted order by ID:");
                for (int i = 0; i < n; i++) {
                    System.out.println("[" + processes[i].index + "] " + processes[i].id);
                }

                while (true) {
                    System.out.println("\n1. Initiate Ring Election");
                    System.out.println("2. Exit");
                    int ch = sc.nextInt();
                    if (ch == 1) {
                        System.out.print("Enter index of initiator: ");
                        int idx = sc.nextInt();
                        if (idx >= n) {
                            System.out.println("Invalid index.");
                            continue;
                        }
                        // Reset message flags
                        for (Process p : processes) p.hasSentMessage = false;
                        ringElection(processes, idx, n);
                    } else if (ch == 2) {
                        System.out.println("Exiting Ring Algorithm.");
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                break;
            }

            default:
                System.out.println("Invalid algorithm selection.");
        }

        sc.close();
    }
}
