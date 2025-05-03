package yee.pltision.maze;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class TestCell implements Cell {
    public Collection<Piece> pieces;

    public TestCell(){
        pieces=new LinkedList<>();

        pieces.add(
                context -> {

                }
        );
    }


    @Override
    public Collection<Piece> pieces() {
        return pieces;
    }
}