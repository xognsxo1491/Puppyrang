package com.portfolio.puppy.util

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * 이메일 전송 API
 * Proguard 수정 필요 */

class EmailUtil {
    /* 변경 필요 */
    private val mEmail: String = "***" // 보내는 이메일
    private val mPw: String = "***" // 이메일 패스워드

    fun sendEmail(
        title: String,      // 메일 제목
        body: String,       // 메일 내용
        dest: String,       // 받는 메일 주소
    ) {
        try {
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.port"] = "587"

            val session = Session.getInstance(props,
                    object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(mEmail, mPw)
                        }
                    })

            val message = MimeMessage(session)
            message.setFrom(InternetAddress(mEmail))

            // 수신자 설정, 여러명으로도 가능
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest))
            message.subject = title
            message.setText(body)

            Transport.send(message)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}