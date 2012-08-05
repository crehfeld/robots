
package pintosim;


public class BasicTweenFactory {
    public enum Algorithm {
        Linear, Quadratic, Cubic, Quint, Sinusoidial, Exponential, Circular, Bounce, Elastic
    }
    public enum Action {
        In, Out, InOut
    }
    
    public Tween create() {
        return create(Algorithm.Linear, Action.InOut);
    }
    
    public Tween create(String algo, String action) {
        return create(strToAlgorithm(algo), strToAction(action));
    }
    
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
        
        throw new UnsupportedOperationException("unknown algo/action combo");
    }
    
    
    private Algorithm strToAlgorithm(String algo) {
        for (Algorithm a : Algorithm.values()) {
            if (a.name().equals(algo)) {
                return a;
            }
        }
        return null;
    }
    
    private Action strToAction(String action) {
        for (Action a : Action.values()) {
            if (a.name().equals(action)) {
                return a;
            }
        }
        return null;
    }
}
