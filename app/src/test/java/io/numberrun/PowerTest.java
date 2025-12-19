package io.numberrun;

import org.junit.jupiter.api.Test;

import io.numberrun.Game.Player.Power;

import static org.junit.jupiter.api.Assertions.*;

class PowerTest {

    @Test
    void testAdd() {
        Power p = new Power(10);
        p.add(5);
        assertEquals(15, p.getValue(), "10 + 5 は 15 になるはず");
    }
    
    // ...existing code...
}