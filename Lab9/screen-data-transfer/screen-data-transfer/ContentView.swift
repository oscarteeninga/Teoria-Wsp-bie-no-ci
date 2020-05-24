//
//  ContentView.swift
//  screen-data-transfer
//
//  Created by Oscar Teeninga on 17/12/2019.
//  Copyright © 2019 Oscar Teeninga. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @State private var showImagePicker: Bool = false
    @State private var image: Image? = nil
    var body: some View {
        VStack {
            image?.resizable().scaledToFit()
            Button("Open camera") {
                self.showImagePicker = true
            }.padding()
                .background(Color.gray)
                .foregroundColor(Color.white)
                .cornerRadius(10)
            
        }.sheet(isPresented: self.$showImagePicker) {
            PhotoCaptureView(showImagePicker: self.$showImagePicker, image: self.$image)
        }
    }
}


#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
#endif
