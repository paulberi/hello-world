package commandoPattern;

import java.util.ArrayList;

public class StringProcessingPipeline {
	
	private ArrayList<StringProcessingCommand> pipeline=new ArrayList<>();
	
	public void addCommand(StringProcessingCommand command) {
		this.pipeline.add(command);
	}
	public String execute(String str) {
		String str1=str;
		for(StringProcessingCommand command:pipeline ) {
			str1=command.process(str1);
		}
		return str1;
	}

}
