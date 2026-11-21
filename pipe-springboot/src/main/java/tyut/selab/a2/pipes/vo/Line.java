package tyut.selab.a2.pipes.vo;

import lombok.Data;

/**
 * 点间的：key-value
 */
@Data
public class Line {
    Long pipeId; //pipe ID
    Coordinate first;
    Coordinate second;
}
