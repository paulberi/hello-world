package repetitionsOvningar;

public class MultiplicationsTabel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//multiplicationsTable(args);
		//anotherMultiTable(args);
		forLoopToGetAlphabet(args);
		
	}

	private static void forLoopToGetAlphabet(String[] args) {
		// TODO Auto-generated method stub
		for(char c='a'; c<='z'; c++) {
			if (c=='z') {
				continue;
				
			}
			System.out.print(c+" , ");
		}
		
	}

	private static void anotherMultiTable(String[] args) {
		// TODO Auto-generated method stub
		for(int i=1;i<=12;i++) {
			for(int j=1; j<=12;j++) {
				System.out.print(i*j+"\t");
				
			}
			System.out.println();
		}
		
	}

	private static void multiplicationsTable(String[] args) {
		// TODO Auto-generated method stub
		for(int i=1;i<=12;i++) {
			for(int j=1; j<=12;j++) {
				int k=i*j;
				System.out.println(i+"*"+j+" = "+k);
			}
		}

	}

}
