import java.io.File;
import java.io.FileReader;
import java.util.*;

class opt_worker
{
    static char[][] precedence=new char [][]{
        {'>','<','<','<','>','>'},
        {'>','>','<','<','>','>'},
        {'>','>','u','u','>','>'},
        {'<','<','<','<','=','u'},
        {'>','>','u','u','>','>'},
        {'<','<','<','<','u','='}
        };
    int ptr=0;
    char[] stack=new char[100000];
    Map <Character,Integer> hash= new HashMap <Character,Integer>();
    void init ()
    {
        hash.put('+',Integer.valueOf(0));
        hash.put('*',Integer.valueOf(1));
        hash.put('i',Integer.valueOf(2));
        hash.put('(',Integer.valueOf(3));
        hash.put(')',Integer.valueOf(4));
        hash.put('#',Integer.valueOf(5));
        stack[ptr]='#';
    }
    void work(char[] buf)
    {
        char ch;
        int sptr;//扫描指针
        boolean error_flag=false;
        for(int i=0;i<buf.length;i++)
        {
            ch=buf[i];
            if(error_flag)
            {
                break;
            }
            if(buf[i]=='\r')
            {
                ch='#';
                error_flag=true;
            }

            if(hash.get(stack[ptr])!=null)
            {
                sptr=ptr;
            }
            else
            {
                sptr=ptr-1;
            }
            if(precedence[hash.get(stack[sptr])][hash.get(ch)]=='>')//试图规约
            {
                while(precedence[hash.get(stack[sptr])][hash.get(ch)]=='>')
                {
                    char Q=stack[sptr];
                    while(!(precedence[hash.get(stack[sptr])][hash.get(Q)]=='<'))
                    {
                        Q=stack[sptr];
                        if(hash.get(stack[sptr-1])!=null)
                        sptr-=1;
                        else 
                        sptr-=2;

                    }
                    if((sptr+1==ptr&&stack[sptr+1]=='i')||
                    (stack[sptr+1]=='N'&&stack[sptr+2]=='+'&&stack[ptr]=='N')||
                    (stack[sptr+1]=='N'&&stack[sptr+2]=='*'&&stack[ptr]=='N')||
                    (stack[sptr+1]=='('&&stack[sptr+2]=='N'&&stack[ptr]==')'))                  
                    {
                        ptr=sptr+1;
                        stack[ptr]='N';
                        System.out.println('R');
                    }
                    else
                    {
                        System.out.println("RE");
                        error_flag=true;
                        break;
                    }

                }
            }
            if(precedence[hash.get(stack[sptr])][hash.get(ch)]=='<'||precedence[hash.get(stack[sptr])][hash.get(ch)]=='=')//小于或等于则移进
            {
                if(ch=='#')
                {
                    break;
                }
                stack[++ptr]=ch;
                System.out.println("I"+ch);
            }
            else if (precedence[hash.get(stack[sptr])][hash.get(ch)]=='u')//未定义
            {
                System.out.println("E");
                error_flag=true;
                break;
            }
            else
            {
                System.out.println('E');
                error_flag=true;
                break;
            }

        }
    }
}
public class opt_analyser
{
    public static void main (String[] args)throws Exception
    {
        File file= new File(args[0]);
        FileReader reader=new FileReader(file);
        int length=(int)file.length();
        char buf[]=new char[length+1];
        reader.read(buf);
        reader.close();
        opt_worker now= new opt_worker();
        now.init();
        now.work(buf);
    }
     
}