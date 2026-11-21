package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.util.List;

/**
 * 节点类
 */
@Data
public class Node {
    Long wellId;
    Coordinate point; //中点
    String model;
    Node left;//左
    Node right;//右
    List<Line> pipes; //关系集合

    public Node(Coordinate point, Long wellId, Node left, Node right, List<Line> pipes,String model) {
        this.point = point;
        this.left = left;
        this.right = right;
        this.wellId = wellId;
        this.pipes = pipes;
        this.model = model;
    }

    public Node() {
    }
}
