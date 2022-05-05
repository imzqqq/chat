{
  description = "The Chat client";

  inputs.nixpkgs.url = github:NixOS/nixpkgs;

  outputs = { self, nixpkgs }: let
    systems = [ "x86_64-linux" "i686-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
    forAllSystems = f: nixpkgs.lib.genAttrs systems (system: f system);

    overlay = import ./nix/overlay.nix;

    # Memoize nixpkgs for different platforms for efficiency.
    nixpkgsFor = forAllSystems (system:
      import nixpkgs {
        inherit system;
        overlays = [ overlay ];
      });
  in {
    inherit overlay;

    packages = builtins.mapAttrs (system: pkgs: {
      inherit (pkgs)
        chat-web
        chat-desktop
        chat-desktop-wayland
      ;
    }) nixpkgsFor;

    defaultPackage = forAllSystems (system: self.packages.${system}.chat-desktop);

    apps = forAllSystems(system: {
      chat-desktop = {
        type = "app";
        program = "${self.packages.${system}.chat-desktop}/bin/chat-desktop";
      };
      chat-desktop-wayland = {
        type = "app";
        program = "${self.packages.${system}.chat-desktop-wayland}/bin/chat-desktop";
      };
    });

    defaultApp = forAllSystems (system: self.apps.${system}.chat-desktop);
  };
}
