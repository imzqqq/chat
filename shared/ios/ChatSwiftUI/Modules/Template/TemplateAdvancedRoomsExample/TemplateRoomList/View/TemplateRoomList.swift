import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomList: View {
    
    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    @ObservedObject var viewModel: TemplateRoomListViewModelType.Context
    
    var body: some View {
        listContent
            .navigationTitle("Unencrypted Rooms")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(VectorL10n.done) {
                        viewModel.send(viewAction: .done)
                    }
                }
            }
    }
    
    @ViewBuilder
    var listContent: some View {
        if viewModel.viewState.rooms.isEmpty {
            Text("No Rooms")
                .foregroundColor(theme.colors.primaryContent)
                .accessibility(identifier: "errorMessage")
        } else {
            ScrollView{
                LazyVStack(spacing: 0) {
                    ForEach(viewModel.viewState.rooms) { room in
                        Button {
                            viewModel.send(viewAction: .didSelectRoom(room.id))
                        } label: {
                            TemplateRoomListRow(avatar: room.avatar, displayName: room.displayName)
                        }
                    }
                }
                .frame(maxHeight: .infinity, alignment: .top)
            }.background(theme.colors.background)
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateRoomList_Previews: PreviewProvider {
    
    static let stateRenderer = MockTemplateRoomListScreenState.stateRenderer
    static var previews: some View {
        stateRenderer.screenGroup(addNavigation: true)
            .theme(.dark)
    }
}
