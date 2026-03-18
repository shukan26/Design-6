/**
 * Uses a queue to store available numbers and a set to track assigned ones.
 * Always allocate from queue and recycle released numbers back into it.
 * Ensures O(1) get, check, and release operations.
 *
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n)
 */
public class PhoneDirectory {

    Queue<Integer> q;
    HashSet<Integer> usedSet;

    public PhoneDirectory(int maxNumbers) {
        this.q = new LinkedList<>();
        this.usedSet = new HashSet<>();

        // Initially, all numbers are available
        for(int i = 0; i < maxNumbers; i++) {
            q.add(i);
        }
    }
    
    public int get() {
        if(q.isEmpty()) return -1; // No available numbers

        int num = q.poll();
        usedSet.add(num); // Maintain invariant: number is now "in use"
        return num;
    }
    
    public boolean check(int number) {
        // A number is available iff it's NOT currently assigned
        return !usedSet.contains(number);
    }
    
    public void release(int number) {
        // Prevent double release (would corrupt queue with duplicates)
        if(!usedSet.contains(number)) return;

        usedSet.remove(number);
        q.add(number); // Recycle back into available pool
    }
}