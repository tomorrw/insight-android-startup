package com.tomorrow.mobile_starter_app.packageImplementation.mappers

import com.tomorrow.components.others.SocialPlatform
import com.tomorrow.kmmProjectStartup.domain.model.SocialLink

fun SocialLink.Platform.toSocialPlatform() = when(this){
    SocialLink.Platform.WhatsApp ->SocialPlatform.WhatsApp
    SocialLink.Platform.Instagram -> SocialPlatform.Instagram
    SocialLink.Platform.Facebook -> SocialPlatform.Facebook
    SocialLink.Platform.LinkedIn -> SocialPlatform.LinkedIn
    SocialLink.Platform.Youtube ->  SocialPlatform.Youtube
    SocialLink.Platform.Twitter ->  SocialPlatform.Twitter
    SocialLink.Platform.Website ->  SocialPlatform.Website
    SocialLink.Platform.Tiktok -> SocialPlatform.Tiktok
    SocialLink.Platform.Phone ->  SocialPlatform.Phone
}