import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day {
    private final CopyOnWriteArrayList<String> al;
    private int errorCount=0;

    public Day(String dayOfWeek) {
        this.al =new CopyOnWriteArrayList<String>();
        for (int i=0;i<8;i++)
        {
            al.add(dayOfWeek+" "+ (i + 10) +": wolne");
        }
        show();
    }
    public String show()
    {
        String a="";
        for (int i=0;i<8;i++)
        {
            a+=al.get(i)+" \n";
        }
        return a;
    }
    public String error()
    {
        if(this.errorCount==1)
        {
            return "\nTermin już zajęty";
        }
        else if(this.errorCount==2)
        {
            return "\nPodano złą godzinę";
        }
        else if(this.errorCount==3)
        {
            return "\nNie można zwolnić wolnego terminu";
        }
        else if(this.errorCount==4)
        {
            return "\nNie można zwolnić terminu innego klienta";
        }
        else{
            return "";
        }
    }
    private void errorZajete()
    {
        this.errorCount=1;
    }
    private void errorZlaGodzina() { this.errorCount=2; }
    private void errorWolne()
    {
        this.errorCount=3;
    }
    private void errorZlyKlient()
    {
        this.errorCount=4;
    }
    public void setDay(String data,long clientId) {
        int number=validate(data);
        if(number==-1)
        {
            return;
        }
            try {
                if(this.al.get(number-10).lastIndexOf("zajęte")!=-1){
                    errorZajete();
                }
                else {
                    StringBuilder b = new StringBuilder(this.al.get(number - 10));
                    b.replace(this.al.get(number - 10).lastIndexOf("w"), this.al.get(number - 10).lastIndexOf("e") + 1, "zajęte przez klienta nr "+clientId);
                    this.al.set(number - 10, b.toString());
                }
            } catch (IndexOutOfBoundsException e) {
                errorZlaGodzina();
            }
    }
    public void unsetDay(String data,Long clientId) {
        int number=validate(data);
        if(number==-1)
        {
            return;
        }
            try {
                String delims = "nr ";
                String[] tokens = this.al.get(number - 10).split(delims);
                if(this.al.get(number-10).lastIndexOf("wolne")!=-1){
                    errorWolne();
                }
                else if(!tokens[1].equals(clientId.toString()))
                {
                    errorZlyKlient();
                }
                else {
                    StringBuilder b = new StringBuilder(this.al.get(number - 10));
                    b.replace(this.al.get(number - 10).lastIndexOf("za"), this.al.get(number - 10).length(), "wolne");
                    this.al.set(number - 10, b.toString());
                }
            } catch (IndexOutOfBoundsException e) {
                errorZlaGodzina();
            }
        }

    private int validate(String data)
    {
        int number = -1;
        this.errorCount=0;
        Pattern numberPat = Pattern.compile("\\d+");
        Matcher matcher1 = numberPat.matcher(data);

        Pattern stringPat = Pattern.compile(data);
        Matcher matcher2 = stringPat.matcher(data);
        if (matcher1.find() && matcher2.find()) {
            number = Integer.parseInt(matcher1.group());
        }
        else
        {
            errorZlaGodzina();
        }
        return number;
    }
}