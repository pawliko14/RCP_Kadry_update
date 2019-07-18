package PDF;

public class Parameters {

	private static String PathToSave= "//192.168.90.203/Logistyka/Listy";
	//private static String PathToDB = "C://xampp/htdocs";
	//private static String PathtoFolder = "//192.168.90.203/DokumentacjaHacoSoft/Listy Produkcja/ListyKOP";
	private static String PathToExportBatch = "V:\\Tosia\\Programy\\RCPprogram\\Pentaho_script\\";
	
	public static String getPathToExportBatch() {
		return PathToExportBatch;
	}

	public static void setPathToExportBatch(String pathToExportBatch) {
		PathToExportBatch = pathToExportBatch;
	}

	public static String getPathToSave(){
		return PathToSave;
	}
	
	public static void setPathToSave (String s){
		PathToSave = s;
	}

//	public static String getPathToDB() {
//		return PathToDB;
//	}
//
//	public static void setPathToDB(String pathToDB) {
//		PathToDB = pathToDB;
//	}

//	public static String getPathtoFolder() {
//		return PathtoFolder;
//	}
//
//	public static void setPathtoFolder(String pathtoFolder) {
//		PathtoFolder = pathtoFolder;
//	}
	
	
}
