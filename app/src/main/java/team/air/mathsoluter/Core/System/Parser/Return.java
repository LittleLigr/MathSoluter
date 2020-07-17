package team.air.mathsoluter.Core.System.Parser;

public class Return extends RuntimeException {
    final Object value;
    public Return(Object value)
    {
        this.value = value;
    }
}
