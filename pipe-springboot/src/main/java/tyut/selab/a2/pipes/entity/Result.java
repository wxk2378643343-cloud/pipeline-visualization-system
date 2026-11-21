package tyut.selab.a2.pipes.entity;

import lombok.Data;

@Data
public class Result {
    public static final Integer SUCCESS = 200;
    public static final Integer ERROR = 500;
    public static final Integer REREQUEST = 114514;
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("OK");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("OK");
        result.setData(data);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMsg("Internal error");
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMsg(msg);
        return result;
    }

    public static Result error(Integer code,String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
