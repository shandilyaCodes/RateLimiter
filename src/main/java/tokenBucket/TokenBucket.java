package rateLimiter;

public class TokenBucket {

    private final long maxBucketSize;
    private final long refillRate; // x req per y sec 

    private double currentBucketSize;
    private long lastRefillTimeStamp;

    public TokenBucket(final long maxBucketSize, final long refillRate) {
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;

        currentBucketSize = maxBucketSize;
        lastRefillTimeStamp = System.nanoTime();
    }

    public boolean allowRequests(int tokens) {
        refill(); // On demand refilling

        if (currentBucketSize > tokens) {
            currentBucketSize -= tokens;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double tokensToAdd = (now - lastRefillTimeStamp) * (refillRate / 1e9);
        currentBucketSize = Math.min(currentBucketSize + tokensToAdd, maxBucketSize);
        lastRefillTimeStamp = now;
    }
}
