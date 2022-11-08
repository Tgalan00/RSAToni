import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

 val ALGORITHM = "RSA"

fun main() {

    println("Hola, Bienvenido")

    println("En cualquier momento que quieras salir introduce 3")

    println("tus claves privadas y publicas son las siguientes:")


    var claves = generateKeys()
    var publica = claves.first
    var privada = claves.second
     var isUserleaving=true

    while (!isUserleaving) {

        println("Quieres descifrar un mensaje o encriptar uno? (1 para descifrar, 2 para recibir")

        val opcio = readln().toIntOrNull()?:0

        if (opcio != 1 || opcio != 2 || opcio != 3) {

            println("Error, las opciones son 1 o 2")

        }

        if (opcio == 1) {


            println("Perfecto, has escogido descifrar un mensaje")

            println("Que quieres escribir? ")

            val decryptmensaje = readln().toString()



            val palabradescifrada= decrypt(privada,decryptmensaje)

            print("$palabradescifrada")



        }

        if (opcio == 2) {

            println("Perfecto, has escogido encriptar un mensaje")

            println("Escribe tu mensaje a encriptar")

            val encryptmensaje = readln().toString()

            println("Cual es tu clave publica? ")

            val publica2 = readln().toString()

            encrypt(publica,encryptmensaje)

        }

        if (opcio == 3) {

            println("Chau")

            isUserleaving=false


        }






    }



}

fun generateKeys(): Pair<String, String> {
    val keyGen = KeyPairGenerator.getInstance(ALGORITHM).apply {
        initialize(512)
    }

    // Key generation
    val keys = keyGen.genKeyPair()

    // Transformation to String (well encoded)
    val publicKeyString = Base64.getEncoder().encodeToString(keys.public.encoded)
    val privateKeyString = Base64.getEncoder().encodeToString(keys.private.encoded)

    return Pair(publicKeyString, privateKeyString)
}

fun encrypt(message: String, publicKey: String): String {
    // From a String, we obtain the Public Key
    val publicBytes = Base64.getDecoder().decode(publicKey)
    val decodedKey = KeyFactory.getInstance(ALGORITHM).generatePublic(X509EncodedKeySpec(publicBytes))

    // With the public, we encrypt the message
    val cipher = Cipher.getInstance(ALGORITHM).apply {
        init(Cipher.ENCRYPT_MODE, decodedKey)
    }
    val bytes = cipher.doFinal(message.encodeToByteArray())
    return String(Base64.getEncoder().encode(bytes))
}

fun decrypt(encryptedMessage: String, privateKey: String): String {
    // From a String, we obtain the Private Key
    val publicBytes = Base64.getDecoder().decode(privateKey)
    val decodedKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(publicBytes))

    // Knowing the Private Key, we can decrypt the message
    val cipher = Cipher.getInstance(ALGORITHM).apply {
        init(Cipher.DECRYPT_MODE, decodedKey)
    }
    val bytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))
    return String(bytes)
}
