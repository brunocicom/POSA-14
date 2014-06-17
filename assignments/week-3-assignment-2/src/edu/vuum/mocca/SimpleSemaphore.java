package edu.vuum.mocca;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject (which is accessed via a Condition). It must
 *        implement both "Fair" and "NonFair" semaphore semantics,
 *        just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
	
	/** Feedback 
	 * A final comment not affecting the score: although it works, 
	 * it is more accurate declaring "private final ReentrantLock lock;" instead of "private final Lock lock;", 
	 * since Lock is an interface, not a class.
	 */
	private final ReentrantLock lock;

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private final Condition isZero;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	
	/** Feedback
	 *  You should either have declared countPermits as volatile, or used lock in availablePermits() method. 
	 *  Currently, it is possible that availablePermits() will return not updated cashed value of countPermits, 
	 *  which is not good. Otherwise, good job! Also as a note, in acquire() method you used lock(), 
	 *  but it is better to use lockInterruptibly()
	 */
	private volatile int countPermits;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	countPermits = permits;
    	lock = new ReentrantLock(fair);
    	isZero = lock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    
    /** Feedback
	 * In the acquire method you should have used "lock.lockInterruptibly();" in order to let the 
	 * lock being unlocked if the thread is interrupted. - You do not define correctly the data member 
	 * that implements the count of available permits (countPermits): it should be volatile in order 
	 * to ensure its integrity during the different operations, otherwise you should have protected its 
	 * access with locks at the method availablePermits.
	 */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	lock.lockInterruptibly();
    	try{
    		while (countPermits <= 0)
    			isZero.await();
    		countPermits--;
    	} finally {
    		lock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	lock.lock();
    	
    	while (countPermits <= 0)
    		isZero.awaitUninterruptibly();
    	countPermits--;
    	
    	lock.unlock();
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
    	lock.lock();
    	
    	countPermits++;
    	isZero.signal();
    	
    	lock.unlock();
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
        return countPermits;
    }
}
