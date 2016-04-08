/**
 * Created by brian.risk on 4/8/16.
 */
public class StockSymbol
{
    String symbol;

    public StockSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockSymbol that = (StockSymbol) o;

        if (!symbol.equals(that.symbol)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return symbol.hashCode();
    }
}
