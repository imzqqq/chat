Pod::Spec.new do |s|
  s.name     = 'Base-16'
  s.version  = '1.0.0'
  s.summary  = "An extension of Swift's Data type to work with hexadecimal (Base16) encoding"
  s.homepage = 'https://github.com/metabolist/base16'
  s.license  = { :type => 'MIT', :file => 'LICENSE' }
  s.author   = { 'Metabolist' => 'info@metabolist.org' }
  s.source   = { :git => 'https://github.com/metabolist/base16.git', :tag => "v#{s.version}" }

  s.module_name = 'Base16'
  s.swift_versions = ['5.2']
  s.ios.deployment_target     = '9.0'
  s.osx.deployment_target     = '10.10'
  s.watchos.deployment_target = '2.0'
  s.tvos.deployment_target    = '9.0'

  s.source_files = 'Sources/Base16/**/*'
end
