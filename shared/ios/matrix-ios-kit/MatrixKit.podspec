Pod::Spec.new do |s|

  s.name         = "MatrixKit"
  s.version      = "0.16.10"
  s.summary      = "The Chat reusable UI library for iOS based on ChatSDK."

  s.description  = <<-DESC
					Chat Kit provides basic reusable interfaces to ease building of apps compatible with Chat (https://www.chat.dingshunyu.top).
                   DESC

  s.platform     = :ios, "12.1"

  s.author             = { "chat.dingshunyu.top" => "support@chat.dingshunyu.top" }
  s.homepage     = "https://www.dingshunyu.top"
  s.social_media_url   = "http://twitter.com/matrixdotorg" 
  s.license      = { :type => "Apache License, Version 2.0", :file => "LICENSE" }
  s.source       = { :git => "https://github.com/imzqqq/matrix-ios-kit.git", :tag => "v#{s.version}" }

  s.requires_arc  = true

  s.swift_version = '5.0'

  ### MARK - FIXME
  s.dependency 'MatrixSDK', "= 0.20.10"
  ###
  s.dependency 'HPGrowingTextView', '~> 1.1'
  s.dependency 'libPhoneNumber-iOS', '~> 0.9.13'
  s.dependency 'DTCoreText', '~> 1.6.25'
  s.dependency 'Down', '~> 0.11.0'

  s.default_subspec = 'Core'

  s.subspec 'Core' do |core|
    core.source_files  = "MatrixKit", "MatrixKit/**/*.{h,m,swift}", "Libs/**/*.{h,m,swift}"
    core.exclude_files = ['MatrixKit/MatrixKit-Bridging-Header.h']
    core.private_header_files = ['MatrixKit/Utils/MXKSwiftHeader.h']
    core.resources = ["MatrixKit/**/*.{xib}", "MatrixKit/Assets/MatrixKitAssets.bundle"]
    core.dependency 'DTCoreText'
  end

  s.subspec 'AppExtension' do |ext|
    ext.source_files  = "MatrixKit", "MatrixKit/**/*.{h,m,swift}", "Libs/**/*.{h,m,swift}"
    ext.exclude_files = ['MatrixKit/MatrixKit-Bridging-Header.h']
    ext.private_header_files = ['MatrixKit/Utils/MXKSwiftHeader.h']
    ext.resources = ["MatrixKit/**/*.{xib}", "MatrixKit/Assets/MatrixKitAssets.bundle"]
    ext.dependency 'DTCoreText/Extension'
  end

end
