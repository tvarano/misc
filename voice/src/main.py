from googlevoice import Voice
import sys

voice = Voice()
voice.login(email='tvarano54@gmail.com', passwd='FruitJuiceButMoreGrainy68%')


def sendText(phoneNumber,text):
    try:
        voice.send_sms(phoneNumber, text)
    except Exception:
        pass

sendText(phoneNumber=[2018873953],text="Hello from Google Voice!")

voice.logout()