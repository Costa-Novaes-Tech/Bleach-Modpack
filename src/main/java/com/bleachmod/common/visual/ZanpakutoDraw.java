package com.bleachmod.common.visual;

/** Pure cosmetic timing; first observation, identity changes and removal never fabricate a draw. */
public class ZanpakutoDraw {
    public int previous = -1, hand = 2, start = Integer.MIN_VALUE;
    public String identity = "";
    public void update(int state, String id, int tick) {
        if (previous != state || !identity.equals(id)) {
            boolean transition = identity.equals(id) && ((previous == 1 && state >= 2) || (previous >= 2 && state == 1));
            start = transition ? tick : Integer.MIN_VALUE;
            if (transition) hand = state >= 2 ? state : previous;
            previous = state; identity = id;
        }
    }
    public float progress(int tick, float partial) {
        return start == Integer.MIN_VALUE ? 1 : Math.max(0, Math.min(1, (tick - start + partial) / 8F));
    }
}
