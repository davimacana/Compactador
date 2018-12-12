package zip.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Davi Maçana
 *
 */
public class App {
	public static void main( String[] args ) {
		zipar("Arquivos", "Arquivos\\");
	}
	
	/**
     * Zipa o arquivo ou diretório
     * @param endEntrada endereço do arquivo ou diretório a ser zipado
     * @param endSaida endereço de saída do arquivo zip gerado
     * @return true se zipou certou ou false se deu erro
     */
    public static boolean zipar(String endEntrada, String endSaida) {
        String dirInterno = "";
        boolean retorno = true;
        try (ZipOutputStream zipDestino = new ZipOutputStream(new FileOutputStream(endSaida + "arquivo.zip"));){
            File file = new File(endEntrada);
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                ziparFile(file, dirInterno, zipDestino);
                file.deleteOnExit();
            } else {
                dirInterno = file.getName();
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                	if (files[i].getName().equalsIgnoreCase("arquivo.zip")) {
                		continue;
                	}
                    ziparFile(files[i], dirInterno, zipDestino);
                    files[i].deleteOnExit();
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
            retorno = false;
        }
        return retorno;
    }
    
    /**
     * Zipa o arquivo ou diretório passado Verifica se é diretório executa recursão para adicionar os arquivos
     * contidos dentro do mesmo no zip senão somente adiciona o arquivo no zip criado
     * @param file arquivo ou diretório a ser adicionado no zip
     * @param dirInterno diretório interno do zip
     * @param zipDestino zip em que está sendo adicionado os arquivos e diretórios
     * @throws java.io.IOException exeção que pode ser gerada na adição de arquivos no zip
     */
    private static void ziparFile(File file, String dirInterno, ZipOutputStream zipDestino) throws IOException {
        byte data[] = new byte[4096];
        try (FileInputStream fi = new FileInputStream(file.getAbsolutePath());) {
        	if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    ziparFile(files[i], dirInterno + File.separator + file.getName(), zipDestino);
                }
                return;
            }
            ZipEntry entry = new ZipEntry(dirInterno + File.separator + file.getName());
            zipDestino.putNextEntry(entry);
            int count;
            while ((count = fi.read(data)) > 0) {
                zipDestino.write(data, 0, count);
            }
            zipDestino.closeEntry();
        } catch (Exception e) {
        	System.err.println(e);
        }
    }
}
