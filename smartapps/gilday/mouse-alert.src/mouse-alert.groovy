/**
 *  Mouse Alert
 *
 *  Copyright 2016 Johnathan Gilday
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Mouse Alert",
    namespace: "gilday",
    author: "Johnathan Gilday",
    description: "Sends an alert when motion is detected in a pantry or cabinet whose doors are closed",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("When motion detected while sensor closed") {
		input "motion", "capability.motionSensor", title: "Motion Here"
        input "openclose", "capability.contactSensor", title: "Contact Closed"
	}
	section("Notify"){
		input "message", "text", title: "Message Text"
        input("recipients", "contact", title: "Send notifications to")
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(motion, "motion.active", onMotionDetected)
}

def onMotionDetected(evt) {
	def contactState = openclose.currentState("contact")
	if (contactState.value == "closed") {
       	log.debug "motion detected, contact is closed, mouse alert sending notifications"
    	sendNotificationToContacts(message, recipients)
    } else {
    	log.debug "motion detected, but contact is ${contactState.value}, therefore do not send alert"
    }
}