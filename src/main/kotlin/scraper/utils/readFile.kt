package scraper.utils

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by andrea on 07/06/16.
 */
fun readFile( path : String, encoding: Charset = Charset.defaultCharset()) : String{
    val encoded = Files.readAllBytes(Paths.get(path))
    return String(encoded,encoding)
}
