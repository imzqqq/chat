import Foundation
import ffmpegkit

enum VoiceMessageAudioConverterError: Error {
    case generic(String)
    case cancelled
}

struct VoiceMessageAudioConverter {
    static func convertToOpusOgg(sourceURL: URL, destinationURL: URL, completion: @escaping (Result<Void, VoiceMessageAudioConverterError>) -> Void) {
        let command = "-hide_banner -y -i \"\(sourceURL.path)\" -c:a libopus -b:a 24k \"\(destinationURL.path)\""
        executeCommand(command, completion: completion)
    }
    
    static func convertToMPEG4AAC(sourceURL: URL, destinationURL: URL, completion: @escaping (Result<Void, VoiceMessageAudioConverterError>) -> Void) {
        let command = "-hide_banner -y -i \"\(sourceURL.path)\" -c:a aac_at \"\(destinationURL.path)\""
        executeCommand(command, completion: completion)
    }
    
    static func mediaDurationAt(_ sourceURL: URL, completion: @escaping (Result<TimeInterval, VoiceMessageAudioConverterError>) -> Void) {
        FFprobeKit.getMediaInformationAsync(sourceURL.path) { session in
            guard let session = session as? MediaInformationSession else {
                completion(.failure(.generic("Invalid session")))
                return
            }
            
            guard let returnCode = session.getReturnCode() else {
                completion(.failure(.generic("Invalid return code")))
                return
            }
            
            DispatchQueue.main.async {
                if returnCode.isSuccess() {
                    let mediaInfo = session.getMediaInformation()
                    if let duration = try? TimeInterval(value: mediaInfo?.getDuration() ?? "0") {
                        completion(.success(duration))
                    } else {
                        completion(.failure(.generic("Failed to get media duration")))
                    }
                } else if returnCode.isCancel() {
                    completion(.failure(.cancelled))
                } else {
                    completion(.failure(.generic(String(returnCode.getValue()))))
                    MXLog.error("""
                        getMediaInformationAsync failed with state: \(String(describing: FFmpegKitConfig.sessionState(toString: session.getState()))), \
                        returnCode: \(String(describing: returnCode)), \
                        stackTrace: \(String(describing: session.getFailStackTrace()))
                        """)
                }
            }
        }
    }
    
    static private func executeCommand(_ command: String, completion: @escaping (Result<Void, VoiceMessageAudioConverterError>) -> Void) {
        FFmpegKitConfig.setLogLevel(0)
        
        FFmpegKit.executeAsync(command) { session in
            guard let session = session else {
                completion(.failure(.generic("Invalid session")))
                return
            }
            
            guard let returnCode = session.getReturnCode() else {
                completion(.failure(.generic("Invalid return code")))
                return
            }
            
            DispatchQueue.main.async {
                if returnCode.isSuccess() {
                    completion(.success(()))
                } else if returnCode.isCancel() {
                    completion(.failure(.cancelled))
                } else {
                    completion(.failure(.generic(String(returnCode.getValue()))))
                    MXLog.error("""
                        Failed converting voice message with state: \(String(describing: FFmpegKitConfig.sessionState(toString: session.getState()))), \
                        returnCode: \(String(describing: returnCode)), \
                        stackTrace: \(String(describing: session.getFailStackTrace()))
                        """)
                }
            }
        }
    }
}
