
package pintosim;


public class TweenFactory {
    public enum Algorithm {
        Linear, Quadratic, Cubic, Quint, Sinusoidial, Exponential, Circular, Bounce, Elastic
    }
    public enum Action {
        In, Out, InOut
    }
    
    /**
     * Returns null when a tween is not yet implemented
     * 
     */
    public Tween create(Algorithm algo, Action action) {
        switch (algo) {
            case Linear:
                return new EaseLinear();

                
            case Quadratic:
                switch (action) {
                    case InOut:
                        return new EaseInOutQuadratic();
                }
                
                break;
                
                
            case Cubic:
                switch (action) {
                    case InOut:
                        return new EaseInOutCubic();
                }
                break;
                
                
            case Exponential:
                switch (action) {
                    case Out:
                        return new EaseOutExponential();
                }
                break;
        }
        
        return null;
    }
}
