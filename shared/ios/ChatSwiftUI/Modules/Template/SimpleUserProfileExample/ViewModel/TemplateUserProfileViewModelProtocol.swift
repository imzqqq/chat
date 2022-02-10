import Foundation

protocol TemplateUserProfileViewModelProtocol {
    
    var completion: ((TemplateUserProfileViewModelResult) -> Void)? { get set }
    @available(iOS 14, *)
    static func makeTemplateUserProfileViewModel(templateUserProfileService: TemplateUserProfileServiceProtocol) -> TemplateUserProfileViewModelProtocol
    @available(iOS 14, *)
    var context: TemplateUserProfileViewModelType.Context { get }
}
